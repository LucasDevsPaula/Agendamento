package com.lucas.agendamento.service;

import com.lucas.agendamento.config.TokenProvider;
import com.lucas.agendamento.database.model.MedicoEntity;
import com.lucas.agendamento.database.model.PacienteEntity;
import com.lucas.agendamento.database.model.RolesEntity;
import com.lucas.agendamento.database.model.UserEntity;
import com.lucas.agendamento.database.repository.MedicoRepository;
import com.lucas.agendamento.database.repository.PacienteRepository;
import com.lucas.agendamento.database.repository.RolesRepossitory;
import com.lucas.agendamento.database.repository.UserRepository;
import com.lucas.agendamento.dto.*;
import com.lucas.agendamento.enums.RoleTypeEnum;
import com.lucas.agendamento.exception.BadRequestException;
import com.lucas.agendamento.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final UserRepository userRepository;
  private final RolesRepossitory rolesRepossitory;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final TokenProvider tokenProvider;
  private final MedicoRepository medicoRepository;
  private final PacienteRepository pacienteRepository;

  @Value("${JWT_EXPIRATION}")
  private long expirationTime;

  public RegisterResponseUserDTO registerUser(RegisterRequestUserDTO dto) {
    UserEntity user = userRepository.findByEmail(dto.getEmail()).orElse(null);

    RoleTypeEnum roleType = dto.getRole() != null ? dto.getRole() : RoleTypeEnum.ROLE_PACIENTE;

    if (user != null) {
      throw new BadRequestException("Usuário já cadastrado com esse e-mail");
    }

    UserEntity newUser =
        userRepository.save(
            UserEntity.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .cpf(dto.getCpf())
                .roles(Set.of(buscarOuCriarRole(roleType)))
                .senha(passwordEncoder.encode(dto.getSenha()))
                .build());

    return RegisterResponseUserDTO.builder()
        .id(newUser.getId())
        .nome(dto.getNome())
        .email(dto.getEmail())
        .build();
  }

  public MedicoResponseDTO registerMedico(Integer idUser, MedicoRequestDTO dto) {
    UserEntity user =
        userRepository
            .findById(idUser)
            .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

    boolean isMedico =
        user.getRoles().stream()
            .anyMatch(role -> role.getNome().equals(RoleTypeEnum.ROLE_MEDICO.name()));

    if (!isMedico) {
      throw new BadRequestException("Usuário não é um médico");
    }

    if (medicoRepository.findByUser(user).isPresent()) {
      throw new BadRequestException("Médico já cadastrado.");
    }

    MedicoEntity medico =
        MedicoEntity.builder()
            .crm(dto.getCrm())
            .especialidade(dto.getEspecialidade())
            .user(user)
            .build();

    medicoRepository.save(medico);
    return MedicoResponseDTO.builder()
        .id(medico.getId())
        .nome(user.getNome())
        .crm(medico.getCrm())
        .especialidade(medico.getEspecialidade())
        .build();
  }

  public PacienteResponseDTO registerPaciente(Integer idUser, PacienteRequestDTO dto){
    UserEntity user =
            userRepository
                    .findById(idUser)
                    .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

    boolean isPaciente =
            user.getRoles().stream()
                    .anyMatch(role -> role.getNome().equals(RoleTypeEnum.ROLE_PACIENTE.name()));

    if (!isPaciente) {
      throw new BadRequestException("Usuário não é um paciente");
    }

    if (pacienteRepository.findByUser(user).isPresent()) {
      throw new BadRequestException("Paciente já cadastrado.");
    }

    PacienteEntity paciente = PacienteEntity.builder()
            .telefone(dto.getTelefone())
            .user(user)
            .build();

    pacienteRepository.save(paciente);

    return PacienteResponseDTO.builder()
            .id(paciente.getId())
            .nome(user.getNome())
            .build();
  }

  private RolesEntity buscarOuCriarRole(RoleTypeEnum role) {
    return rolesRepossitory
        .findByNome(role.name())
        .orElseGet(() -> rolesRepossitory.save(RolesEntity.builder().nome(role.name()).build()));
  }

  public TokenResponseDTO login(LoginRequestDTO dto) {
    try {
      Authentication authentication =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getSenha()));

      String token = tokenProvider.gerarToken(authentication);

      return new TokenResponseDTO(token, expirationTime);
    } catch (BadRequestException e) {
      throw new BadRequestException("Credenciais inválidas");
    }
  }
}
