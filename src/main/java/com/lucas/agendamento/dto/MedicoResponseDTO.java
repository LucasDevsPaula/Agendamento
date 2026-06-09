package com.lucas.agendamento.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicoResponseDTO {
    private Integer id;
    private String nome;
    private String crm;
    private String especialidade;
}
