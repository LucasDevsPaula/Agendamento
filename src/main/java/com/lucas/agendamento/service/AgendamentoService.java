package com.lucas.agendamento.service;

import com.lucas.agendamento.database.model.MedicoEntity;
import com.lucas.agendamento.database.model.PacienteEntity;
import com.lucas.agendamento.database.model.UserEntity;
import com.lucas.agendamento.database.repository.DisponibilidadeRepository;
import com.lucas.agendamento.database.repository.MedicoRepository;
import com.lucas.agendamento.database.repository.PacienteRepository;
import com.lucas.agendamento.dto.agendamento.AgendamentoRequestDTO;
import com.lucas.agendamento.dto.agendamento.AgendamentoResponseDTO;
import com.lucas.agendamento.enums.SemanaTypeEnum;
import com.lucas.agendamento.exception.BadRequestException;
import com.lucas.agendamento.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AgendamentoService {
  private final MedicoRepository medicoRepository;
  private final PacienteRepository pacienteRepository;
  private final DisponibilidadeRepository disponibilidadeRepository;

  public AgendamentoResponseDTO agendar(AgendamentoRequestDTO dto) {
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


  }

  private SemanaTypeEnum SconvertDiaSemana(LocalDateTime inicio){
    return switch (inicio.getDayOfWeek()){
      case MONDAY -> SemanaTypeEnum.SEGUNDA;
      case TUESDAY -> SemanaTypeEnum.TERCA;
      case WEDNESDAY -> SemanaTypeEnum.QUARTA;
      case THURSDAY -> SemanaTypeEnum.QUINTA;
      case FRIDAY -> SemanaTypeEnum.SEXTA;
      default -> throw new BadRequestException("Agendamento permitido somente entre segunda e sexta-feira");
    };
  }

  private
}
