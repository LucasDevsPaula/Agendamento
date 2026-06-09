package com.lucas.agendamento.dto;

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
