package com.lucas.agendamento.service;

import com.lucas.agendamento.database.model.*;
import com.lucas.agendamento.database.repository.ConsultaRepository;
import com.lucas.agendamento.database.repository.DisponibilidadeRepository;
import com.lucas.agendamento.database.repository.MedicoRepository;
import com.lucas.agendamento.database.repository.PacienteRepository;
import com.lucas.agendamento.dto.consulta.*;
import com.lucas.agendamento.enums.RoleTypeEnum;
import com.lucas.agendamento.enums.SemanaTypeEnum;
import com.lucas.agendamento.enums.StatusTypeEnum;
import com.lucas.agendamento.exception.BadRequestException;
import com.lucas.agendamento.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultaService {
  private final MedicoRepository medicoRepository;
  private final PacienteRepository pacienteRepository;
  private final DisponibilidadeRepository disponibilidadeRepository;
  private final ConsultaRepository consultaRepository;

  public ConsultaResponseDTO agendar(ConsultaRequestDTO dto) {
    MedicoEntity medico =
        medicoRepository
            .findById(dto.getMedicoId())
            .orElseThrow(
                () ->
                    new NotFoundException("Médico não encontrado com o ID: " + dto.getMedicoId()));

    UserEntity userLogado =
        (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    PacienteEntity paciente =
        pacienteRepository
            .findByUser(userLogado)
            .orElseThrow(
                () -> new NotFoundException("Paciente não encontrado para o usuário logado"));

    LocalDateTime inicio = dto.getHoraInicio();
    LocalDateTime fim = dto.getHoraInicio().plusHours(1);

    validarHorario(inicio, fim, medico, paciente);

    ConsultaEntity consulta =
        consultaRepository.save(
            ConsultaEntity.builder()
                .dataHoraInicio(inicio)
                .dataHoraFim(fim)
                .status(StatusTypeEnum.AGENDADA)
                .medico(medico)
                .paciente(paciente)
                .observacao(dto.getObservacao())
                .build());

    return ConsultaResponseDTO.builder()
        .id(consulta.getId())
        .nome(consulta.getPaciente().getUser().getNome())
        .cpf(consulta.getPaciente().getUser().getCpf())
        .nomeMedico(consulta.getMedico().getUser().getNome())
        .crm(consulta.getMedico().getCrm())
        .observacao(consulta.getObservacao())
        .horarioInicio(consulta.getDataHoraInicio())
        .horarioFim(consulta.getDataHoraFim())
        .status(consulta.getStatus())
        .build();
  }

  private SemanaTypeEnum ConvertDiaSemana(LocalDateTime inicio) {
    return switch (inicio.getDayOfWeek()) {
      case MONDAY -> SemanaTypeEnum.SEGUNDA;
      case TUESDAY -> SemanaTypeEnum.TERCA;
      case WEDNESDAY -> SemanaTypeEnum.QUARTA;
      case THURSDAY -> SemanaTypeEnum.QUINTA;
      case FRIDAY -> SemanaTypeEnum.SEXTA;
      default ->
          throw new BadRequestException(
              "Agendamento permitido somente entre segunda e sexta-feira");
    };
  }

  private void validarHorario(
      LocalDateTime inicio, LocalDateTime fim, MedicoEntity medico, PacienteEntity paciente) {
    SemanaTypeEnum semana = ConvertDiaSemana(inicio);

    DisponibilidadeMedicoEntity disponibilidade =
        disponibilidadeRepository
            .findByMedicoIdAndDiaSemana(medico.getId(), semana)
            .orElseThrow(
                () ->
                    new BadRequestException(
                        "Médico não possui disponibilidade nesse dia da semana"));

    LocalTime horaInicio = inicio.toLocalTime();
    LocalTime horaFim = fim.toLocalTime();

    if (horaInicio.isBefore(disponibilidade.getHoraInicio())
        || horaFim.isAfter(disponibilidade.getHoraFim())) {
      throw new BadRequestException(
          "Horário fora da disponibilidade do médico. Disponibilidade de "
              + disponibilidade.getHoraInicio()
              + " até "
              + disponibilidade.getHoraFim());
    }

    consultaRepository
        .findConflitoMedico(medico.getId(), inicio, fim, StatusTypeEnum.AGENDADA)
        .ifPresent(
            c -> {
              throw new BadRequestException("Médico já possui consulta nesse horário");
            });

    consultaRepository
        .findConflitoPaciente(paciente.getId(), inicio, fim, StatusTypeEnum.AGENDADA)
        .ifPresent(
            c -> {
              throw new BadRequestException("Você já possui uma consulta nesse horário");
            });
  }

  public ConsultaResponseDTO editatAgendamento(Integer consultaId, ConsultaUpdateRequestDTO dto) {
    ConsultaEntity consulta =
        consultaRepository
            .findById(consultaId)
            .orElseThrow(() -> new NotFoundException("A consulta não existe."));

    UserEntity userLogado =
        (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    boolean isAdminOuMedico =
        userLogado.getRoles().stream()
            .anyMatch(
                roles ->
                    roles.getNome().equals(RoleTypeEnum.ROLE_ADMIN.name())
                        || roles.getNome().equals(RoleTypeEnum.ROLE_MEDICO.name()));

    if (!isAdminOuMedico) {
      PacienteEntity paciente =
          pacienteRepository
              .findByUser(userLogado)
              .orElseThrow(() -> new NotFoundException("Paciente não encontrado"));

      if (!consulta.getPaciente().getId().equals(paciente.getId())) {
        throw new BadRequestException("Você não tem permissão para editar essa consulta.");
      }
    }

    if (dto.getHoraInicio() != null) {
      LocalDateTime novoInicio = dto.getHoraInicio();
      LocalDateTime novoFim = novoInicio.plusHours(1);
      validarHorario(novoInicio, novoFim, consulta.getMedico(), consulta.getPaciente());
      consulta.setDataHoraInicio(novoInicio);
      consulta.setDataHoraFim(novoFim);
    }

    if (dto.getObservacao() != null) {
      consulta.setObservacao(dto.getObservacao());
    }

    if (dto.getStatus() != null) {
      consulta.setStatus(dto.getStatus());
    }

    consultaRepository.save(consulta);
    return ConsultaResponseDTO.builder()
        .id(consulta.getId())
        .nome(consulta.getPaciente().getUser().getNome())
        .cpf(consulta.getPaciente().getUser().getCpf())
        .nomeMedico(consulta.getMedico().getUser().getNome())
        .crm(consulta.getMedico().getCrm())
        .observacao(consulta.getObservacao())
        .horarioInicio(consulta.getDataHoraInicio())
        .horarioFim(consulta.getDataHoraFim())
        .status(consulta.getStatus())
        .build();
  }

  public ConsultaCanceladoResponseDTO cancelarAgendamento(Integer idConsulta) {
    ConsultaEntity consulta =
        consultaRepository
            .findById(idConsulta)
            .orElseThrow(() -> new NotFoundException("Consulta não encontrada"));

    UserEntity pacienteLogado =
        (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    boolean isAdminOuMedico =
        pacienteLogado.getRoles().stream()
            .anyMatch(
                roles ->
                    roles.getNome().equals(RoleTypeEnum.ROLE_ADMIN.name())
                        || roles.getNome().equals(RoleTypeEnum.ROLE_MEDICO.name()));

    if (!isAdminOuMedico) {
      PacienteEntity paciente =
          pacienteRepository
              .findByUser(pacienteLogado)
              .orElseThrow(() -> new NotFoundException("Paciente não encontrado"));

      if (!consulta.getPaciente().getId().equals(paciente.getId())) {
        throw new BadRequestException("Você não tem permissão para editar essa consulta.");
      }
    }
    if (consulta.getStatus() != StatusTypeEnum.AGENDADA) {
      throw new BadRequestException("Apenas consultas agendadas podem ser canceladas");
    }

    consulta.setStatus(StatusTypeEnum.CANCELADA);
    consultaRepository.save(consulta);

    return ConsultaCanceladoResponseDTO.builder()
        .id(consulta.getId())
        .nome(consulta.getPaciente().getUser().getNome())
        .nomeMedico(consulta.getMedico().getUser().getNome())
        .crm(consulta.getMedico().getCrm())
        .horarioInicio(consulta.getDataHoraInicio())
        .horarioFim(consulta.getDataHoraFim())
        .status(consulta.getStatus())
        .build();
  }

  public Page<ConsultaMedicoResponseDTO> listarConsultasMedico(
      Integer medicoId, Pageable pageable) {
    UserEntity user =
        (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    boolean isAdmin =
        user.getRoles().stream().anyMatch(r -> r.getNome().equals(RoleTypeEnum.ROLE_ADMIN.name()));
    if (!isAdmin) {
      MedicoEntity medico =
          medicoRepository
              .findByUser(user)
              .orElseThrow(() -> new NotFoundException("Médico não encontrado"));

      if (!medico.getId().equals(medicoId)) {
        throw new BadRequestException("Você não pode vizualizar as consultas desse médico.");
      }
    }

    Page<ConsultaEntity> consultas = consultaRepository.findAllByMedicoId(medicoId, pageable);
    return consultas.map(
        consultaEntity ->
            ConsultaMedicoResponseDTO.builder()
                .nome(consultaEntity.getPaciente().getUser().getNome())
                .cpf(consultaEntity.getPaciente().getUser().getCpf())
                .horarioInicio(consultaEntity.getDataHoraInicio())
                .horarioFim(consultaEntity.getDataHoraFim())
                .observacao(consultaEntity.getObservacao())
                .status(consultaEntity.getStatus())
                .build());
  }

  public Page<ConsultaPacienteResponseDTO> listarConsultasPaciente(
      Integer pacienteId, Pageable pageable) {
    UserEntity user =
        (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    boolean isAdmin =
        user.getRoles().stream().anyMatch(r -> r.getNome().equals(RoleTypeEnum.ROLE_ADMIN.name()));
    if (!isAdmin) {
      PacienteEntity paciente =
          pacienteRepository
              .findByUser(user)
              .orElseThrow(() -> new NotFoundException("Paciente não encontrado"));

      if (!paciente.getId().equals(pacienteId)) {
        throw new BadRequestException("Você não pode vizualizar as consultas de outros pacientes.");
      }
    }

    Page<ConsultaEntity> consultas = consultaRepository.findAllByPacienteId(pacienteId, pageable);
    return consultas.map(
        consultaEntity ->
            ConsultaPacienteResponseDTO.builder()
                .nomeMedico(consultaEntity.getMedico().getUser().getNome())
                .crm(consultaEntity.getMedico().getCrm())
                .observacao(consultaEntity.getObservacao())
                .status(consultaEntity.getStatus())
                .horarioInicio(consultaEntity.getDataHoraInicio())
                .horarioFim(consultaEntity.getDataHoraFim())
                .build());
  }

  public List<ConsultaResponseDTO> listarConsultasConfirmadas(Integer medicoId) {
    UserEntity userLogado =
        (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    boolean isAdmin =
        userLogado.getRoles().stream()
            .anyMatch(role -> role.getNome().equals(RoleTypeEnum.ROLE_ADMIN.name()));

    if (!isAdmin) {
      MedicoEntity medico =
          medicoRepository
              .findByUser(userLogado)
              .orElseThrow(() -> new NotFoundException("Médico não encontrado"));

      if (!medico.getId().equals(medicoId)) {
        throw new BadRequestException(
            "Você não tem permissão para visualizar as consultas desse médico");
      }
    }

    return consultaRepository
        .findAllByMedicoIdAndStatus(medicoId, StatusTypeEnum.CONFIRMADA)
        .stream()
        .map(
            consulta ->
                ConsultaResponseDTO.builder()
                    .id(consulta.getId())
                    .nome(consulta.getPaciente().getUser().getNome())
                    .cpf(consulta.getPaciente().getUser().getCpf())
                    .nomeMedico(consulta.getMedico().getUser().getNome())
                    .crm(consulta.getMedico().getCrm())
                    .observacao(consulta.getObservacao())
                    .horarioInicio(consulta.getDataHoraInicio())
                    .horarioFim(consulta.getDataHoraFim())
                    .status(consulta.getStatus())
                    .build())
        .toList();
  }
}
