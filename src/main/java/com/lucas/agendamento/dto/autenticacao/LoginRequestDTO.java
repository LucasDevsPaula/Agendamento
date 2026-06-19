package com.lucas.agendamento.dto.autenticacao;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Validated
public class LoginRequestDTO {
  @NotEmpty private String email;
  @NotEmpty private String senha;
}
