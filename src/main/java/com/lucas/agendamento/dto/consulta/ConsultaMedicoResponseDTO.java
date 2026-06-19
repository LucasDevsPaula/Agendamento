package com.lucas.agendamento.dto.consulta;

import com.lucas.agendamento.enums.StatusTypeEnum;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Validated
public class ConsultaMedicoResponseDTO {
    private String nome;
    private String cpf;
    private String observacao;
    private LocalDateTime horarioInicio;
    private LocalDateTime horarioFim;
    private StatusTypeEnum status;
}
