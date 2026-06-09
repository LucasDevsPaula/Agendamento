package com.lucas.agendamento.controller;

import com.lucas.agendamento.dto.*;
import com.lucas.agendamento.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
  private final AuthenticationService authenticationService;

  @PostMapping("/register")
  public ResponseEntity<RegisterResponseUserDTO> register(
      @Valid @RequestBody RegisterRequestUserDTO dto) throws URISyntaxException {
    return ResponseEntity.created(new URI("/v1/auth"))
        .body(authenticationService.registerUser(dto));
  }

  @PostMapping("/login")
  public ResponseEntity<TokenResponseDTO> logar(@Valid @RequestBody LoginRequestDTO dto) {
    return ResponseEntity.ok().body(authenticationService.login(dto));
  }

  @PostMapping("/registerMedico/{idUser}")
  public ResponseEntity<MedicoResponseDTO> registerMedico(
      @PathVariable Integer idUser, @Valid @RequestBody MedicoRequestDTO dto)
      throws URISyntaxException {
    return ResponseEntity.created(new URI("/v1/auth/registerMedico"))
        .body(authenticationService.registerMedico(idUser, dto));
  }

  @PostMapping("/registerPaciente/{idUser}")
  public ResponseEntity<PacienteResponseDTO> registerPaciente(
      @PathVariable Integer idUser, @Valid @RequestBody PacienteRequestDTO dto) {
    return ResponseEntity.created(URI.create("/v1/auth/registerPaciente"))
        .body(authenticationService.registerPaciente(idUser, dto));
  }
}
