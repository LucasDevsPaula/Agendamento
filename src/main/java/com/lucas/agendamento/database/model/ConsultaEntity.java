package com.lucas.agendamento.database.model;

import com.lucas.agendamento.enums.StatusTypeEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "consulta")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConsultaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name = "data_hora_inicio")
    private LocalDateTime dataHoraInicio;

    @Column(name = "data_hora_fim")
    private LocalDateTime dataHoraFim;
    private StatusTypeEnum status;

    @ManyToOne
    @JoinColumn(name = "medico_id")
    private MedicoEntity medico;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private PacienteEntity paciente;

    private String observacao;
}
