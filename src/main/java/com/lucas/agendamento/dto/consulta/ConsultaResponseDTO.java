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
public class ConsultaResponseDTO {
    private Integer id;
    private String nome;
    private String cpf;
    private String nomeMedico;
    private String crm;
    private String observacao;
    private LocalDateTime horarioInicio;
    private LocalDateTime horarioFim;
    private StatusTypeEnum status;
}
