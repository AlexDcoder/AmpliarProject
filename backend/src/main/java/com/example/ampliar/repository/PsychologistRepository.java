package com.example.ampliar.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ampliar.model.PsychologistModel;

public interface PsychologistRepository extends JpaRepository<PsychologistModel, Long> {
    Optional<PsychologistModel> findByEmail(String email);

    Optional<PsychologistModel> findByCpf(String cpf);
    
}
