package com.lucas.agendamento.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Validated
public class MedicoRequestDTO {
    @NotEmpty private String crm;
    @NotEmpty private String especialidade;
}
