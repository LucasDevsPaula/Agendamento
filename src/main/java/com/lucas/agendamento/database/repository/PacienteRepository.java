package com.lucas.agendamento.database.repository;

import com.lucas.agendamento.database.model.PacienteEntity;
import com.lucas.agendamento.database.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<PacienteEntity, Integer> {
  Optional<PacienteEntity> findByUser(UserEntity user);
}
