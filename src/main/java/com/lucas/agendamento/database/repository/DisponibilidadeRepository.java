package com.lucas.agendamento.database.repository;

import com.lucas.agendamento.database.model.DisponibilidadeMedicoEntity;
import com.lucas.agendamento.enums.SemanaTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DisponibilidadeRepository extends JpaRepository<DisponibilidadeMedicoEntity, Integer> {
    Optional<DisponibilidadeMedicoEntity> findByMedicoIdAndDiaSemana(Integer medicoId, SemanaTypeEnum semana);
}
