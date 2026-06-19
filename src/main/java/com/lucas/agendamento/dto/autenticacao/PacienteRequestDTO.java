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
public class PacienteRequestDTO {
    @NotEmpty private String telefone;
}
