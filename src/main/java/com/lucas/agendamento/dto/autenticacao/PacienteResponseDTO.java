package com.lucas.agendamento.dto.autenticacao;

import lombok.*;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Validated
public class PacienteResponseDTO {
    private Integer id;
    private String nome;
}
