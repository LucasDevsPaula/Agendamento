package com.lucas.agendamento.database.repository;

import com.lucas.agendamento.database.model.ConsultaEntity;
import com.lucas.agendamento.enums.StatusTypeEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConsultaRepository extends JpaRepository<ConsultaEntity, Integer> {
  @Query(
              """
                  SELECT c FROM ConsultaEntity c
                  WHERE c.medico.id = :pacienteId
                  AND c.status = :status
                  AND c.dataHoraInicio < :fim
                  AND c.dataHoraFim > :inicio
              """)
  Optional<ConsultaEntity> findConflitoPaciente(
      @Param("pacienteId") Integer pacienteId,
      @Param("inicio") LocalDateTime inicio,
      @Param("fim") LocalDateTime fim,
      @Param("status") StatusTypeEnum status);

  @Query(
                  """
                      SELECT c FROM ConsultaEntity c
                      WHERE c.medico.id = :medicoId
                      AND c.status = :status
                      AND c.dataHoraInicio < :fim
                      AND c.dataHoraFim > :inicio
                  """)
  Optional<ConsultaEntity> findConflitoMedico(
      @Param("medicoId") Integer medicoId,
      @Param("inicio") LocalDateTime inicio,
      @Param("fim") LocalDateTime fim,
      @Param("status") StatusTypeEnum status);

  Page<ConsultaEntity> findAllByMedicoId(Integer medicoId, Pageable pageable);

  Page<ConsultaEntity> findAllByPacienteId(Integer pacienteId, Pageable pageable);

  List<ConsultaEntity> findAllByMedicoIdAndStatus(Integer medicoId, StatusTypeEnum status);
}
