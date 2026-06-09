package com.lucas.agendamento.database.repository;

import com.lucas.agendamento.database.model.DisponibilidadeMedicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisponibilidadeRepository extends JpaRepository<DisponibilidadeMedicoEntity, Integer> {}
