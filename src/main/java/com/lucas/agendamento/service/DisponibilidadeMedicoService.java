package com.lucas.agendamento.service;

import com.lucas.agendamento.database.model.DisponibilidadeMedicoEntity;
import com.lucas.agendamento.database.model.MedicoEntity;
import com.lucas.agendamento.database.model.UserEntity;
import com.lucas.agendamento.database.repository.ConsultaRepository;
import com.lucas.agendamento.database.repository.DisponibilidadeRepository;
import com.lucas.agendamento.database.repository.MedicoRepository;
import com.lucas.agendamento.dto.consulta.ConsultaResponseDTO;
import com.lucas.agendamento.dto.disponibilidade.DisponibilidadeMedicoRequestDTO;
import com.lucas.agendamento.dto.disponibilidade.MedicoResponseDTO;
import com.lucas.agendamento.enums.RoleTypeEnum;
import com.lucas.agendamento.enums.StatusTypeEnum;
import com.lucas.agendamento.exception.BadRequestException;
import com.lucas.agendamento.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DisponibilidadeMedicoService {

  private final DisponibilidadeRepository disponibilidadeRepository;
  private final MedicoRepository medicoRepository;
  private final ConsultaRepository consultaRepository;

  public DisponibilidadeMedicoRequestDTO cadastrarAgendaMedico(
      Integer medicoId, DisponibilidadeMedicoRequestDTO dto) {
    MedicoEntity medico =
        medicoRepository
            .findById(medicoId)
            .orElseThrow(() -> new NotFoundException("Médico não encontrado"));

    disponibilidadeRepository.save(
        DisponibilidadeMedicoEntity.builder()
            .diaSemana(dto.getDiaSemana())
            .horaInicio(dto.getHoraInicio())
            .horaFim(dto.getHoraFim())
            .medico(medico)
            .build());

    return dto;
  }

  public List<MedicoResponseDTO> listarMedicos() {
    List<MedicoEntity> medicos = medicoRepository.findAll();
    return medicos.stream()
        .map(
            medico ->
                MedicoResponseDTO.builder()
                    .nome(medico.getUser().getNome())
                    .especialidade(medico.getEspecialidade())
                    .crm(medico.getCrm())
                    .build())
        .toList();
  }


}
