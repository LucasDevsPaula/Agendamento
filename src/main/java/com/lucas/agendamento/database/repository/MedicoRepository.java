package com.lucas.agendamento.database.repository;

import com.lucas.agendamento.database.model.MedicoEntity;
import com.lucas.agendamento.database.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicoRepository extends JpaRepository<MedicoEntity, Integer> {
  Optional<MedicoEntity> findByUser(UserEntity user);


}
