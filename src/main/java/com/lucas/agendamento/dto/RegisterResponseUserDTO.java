package com.lucas.agendamento.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterResponseUserDTO {
    private Integer id;
    private String nome;
    private String email;
}
