package com.lucas.agendamento.controller;

import com.lucas.agendamento.dto.disponibilidade.DisponibilidadeMedicoRequestDTO;
import com.lucas.agendamento.dto.disponibilidade.MedicoResponseDTO;
import com.lucas.agendamento.service.DisponibilidadeMedicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/medicos")
@RequiredArgsConstructor
public class DisponibilidadeMedicoController {

  private final DisponibilidadeMedicoService disponibilidadeMedicoService;

  @PostMapping("/{medicoId}/agenda")
  public ResponseEntity<DisponibilidadeMedicoRequestDTO> cadastrarAgendaMedico(
      @PathVariable Integer medicoId, @RequestBody @Valid DisponibilidadeMedicoRequestDTO dto) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(disponibilidadeMedicoService.cadastrarAgendaMedico(medicoId, dto));
  }

  @GetMapping
  public ResponseEntity<List<MedicoResponseDTO>> listarMedicos() {
    return ResponseEntity.status(HttpStatus.OK).body(disponibilidadeMedicoService.listarMedicos());
  }

}
