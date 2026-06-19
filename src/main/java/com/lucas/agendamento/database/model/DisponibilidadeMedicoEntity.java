package com.lucas.agendamento.database.model;

import com.lucas.agendamento.enums.SemanaTypeEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(name = "disponibilidade_medico")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DisponibilidadeMedicoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "dia_semana")
    private SemanaTypeEnum diaSemana;

    @Column(name = "hora_inicio")
    private LocalTime horaInicio;

    @Column(name = "hora_fim")
    private LocalTime horaFim;

    @ManyToOne
    @JoinColumn(name = "medico_id")
    private MedicoEntity medico;
}
