package com.lucas.agendamento.dto.autenticacao;

import com.lucas.agendamento.enums.RoleTypeEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Validated
public class RegisterRequestUserDTO {
  @NotBlank private String nome;
  @NotBlank private String email;
  @NotBlank private String senha;
  @NotBlank private String cpf;
  private RoleTypeEnum role;
}
