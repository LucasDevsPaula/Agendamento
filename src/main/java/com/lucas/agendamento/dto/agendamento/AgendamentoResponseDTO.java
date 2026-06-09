package com.lucas.agendamento.dto.agendamento;

import com.lucas.agendamento.enums.StatusTypeEnum;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Validated
public class AgendamentoResponseDTO {
    private Integer id;
    private String nome;
    private String cpf;
    private String nomeMedico;
    private String observacao;
    private LocalDateTime horarioInicio;
    private LocalDateTime horarioFim;
    private StatusTypeEnum status;
}
