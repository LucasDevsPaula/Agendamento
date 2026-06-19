package com.lucas.agendamento.dto.consulta;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Validated
public class ConsultaRequestDTO {
    @NonNull private Integer medicoId;
    @NotNull private LocalDateTime horaInicio;
    private String observacao;
}
