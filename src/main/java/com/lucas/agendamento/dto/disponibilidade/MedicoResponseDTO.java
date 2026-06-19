package com.lucas.agendamento.dto.disponibilidade;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicoResponseDTO {
    private String nome;
    private String especialidade;
    private String crm;
}
