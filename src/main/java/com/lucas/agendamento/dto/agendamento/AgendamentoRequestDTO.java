package com.lucas.agendamento.dto.agendamento;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Validated
public class AgendamentoRequestDTO {
    @NonNull private Integer medicoId;
    @NotEmpty private LocalDateTime horaInicio;
    private String observacao;
}
