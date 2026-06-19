package com.lucas.agendamento.dto.disponibilidade;

import com.lucas.agendamento.enums.SemanaTypeEnum;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DisponibilidadeMedicoRequestDTO {
    @NotNull private SemanaTypeEnum diaSemana;
    @NotNull private LocalTime horaInicio;
    @NotNull private LocalTime horaFim;
}
