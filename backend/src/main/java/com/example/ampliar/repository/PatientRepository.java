package com.example.ampliar.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ampliar.model.PatientModel;

public interface PatientRepository extends JpaRepository<PatientModel, Long>{

    // MÉTODOS ADICIONADOS
    List<PatientModel> findByPsychologistId(Long psychologistId);
    Optional<PatientModel> findByIdAndPsychologistId(Long id, Long psychologistId);
}
