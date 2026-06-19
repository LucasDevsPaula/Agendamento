package com.lucas.agendamento.controller;

import com.lucas.agendamento.dto.consulta.*;
import com.lucas.agendamento.service.ConsultaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/consultas")
@RequiredArgsConstructor
public class ConsultaController {

  private final ConsultaService consultaService;

  @PostMapping("/agendar")
  public ResponseEntity<ConsultaResponseDTO> agendar(@RequestBody @Valid ConsultaRequestDTO dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(consultaService.agendar(dto));
  }

  @PatchMapping("/{consultaId}")
  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MEDICO', 'ROLE_PACIENTE')")
  public ResponseEntity<ConsultaResponseDTO> editarAgendamento(
      @PathVariable Integer consultaId, @RequestBody @Valid ConsultaUpdateRequestDTO dto) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(consultaService.editatAgendamento(consultaId, dto));
  }

  @PutMapping("/cancelar/{consultaId}")
  @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'PACIENTE')")
  public ResponseEntity<ConsultaCanceladoResponseDTO> cancelarAgendamento(
      @PathVariable Integer consultaId) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(consultaService.cancelarAgendamento(consultaId));
  }

  @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
  @GetMapping("/listar/medico/{medicoId}")
  public ResponseEntity<Page<ConsultaMedicoResponseDTO>> listarConsultasMedico(
      @PathVariable Integer medicoId,
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "2") Integer size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("dataHoraInicio").ascending());
    return ResponseEntity.status(HttpStatus.OK)
        .body(consultaService.listarConsultasMedico(medicoId, pageable));
  }

  @PreAuthorize("hasAnyRole('ADMIN', 'PACIENTE')")
  @GetMapping("/listar/paciente/{pacienteId}")
  public ResponseEntity<Page<ConsultaPacienteResponseDTO>> listarConsultasPaciente(
      @PathVariable Integer pacienteId,
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "2") Integer size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("dataHoraInicio").ascending());
    return ResponseEntity.status(HttpStatus.OK)
        .body(consultaService.listarConsultasPaciente(pacienteId, pageable));
  }

  @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
  @GetMapping("/{medicoId}")
  public ResponseEntity<List<ConsultaResponseDTO>> listarConsultasConfirmadas(@PathVariable Integer medicoId){
    return ResponseEntity.status(HttpStatus.OK).body(consultaService.listarConsultasConfirmadas(medicoId));
  }
}
