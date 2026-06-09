package com.lucas.agendamento.database.repository;

import com.lucas.agendamento.database.model.ConsultaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ConsultaRepository extends JpaRepository<ConsultaEntity, Integer> {
  @Query(
"""
    SELECT c FROM CnsultaEntity c
    WHERE c.medico.id = :medicoId
    AND c.status = 'AGENDADA'
    AND c.inicio < :fim
    AND c.fim > :inicio
""")
  Optional<ConsultaEntity> findConflitoMedico(
      @Param("medicoId") Integer medicoId,
      @Param("inicio") LocalDateTime inicio,
      @Param("fim") LocalDateTime fim);

  @Query(
      """
              SELECT c FROM CnsultaEntity c
              WHERE c.medico.id = :pacienteId
              AND c.status = 'AGENDADA'
              AND c.inicio < :fim
              AND c.fim > :inicio
          """)
  Optional<ConsultaEntity> findConflitoPaciente(
      @Param("pacienteId") Integer pacienteId,
      @Param("inicio") LocalDateTime inicio,
      @Param("fim") LocalDateTime fim);
}
