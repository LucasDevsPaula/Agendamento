package com.lucas.agendamento.dto.consulta;

import com.lucas.agendamento.enums.StatusTypeEnum;

import java.time.LocalDateTime;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Validated
public class ConsultaUpdateRequestDTO {
    private LocalDateTime horaInicio;
    private StatusTypeEnum status;
    private String observacao;
}
