package com.example.ampliar.repository;

import java.util.List;
import java.util.Optional; // IMPORTAR

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ampliar.model.PatientModel; // IMPORTAR
import com.example.ampliar.model.PsychologistModel; // IMPORTAR

// ****** INÍCIO DA MODIFICAÇÃO ******

public interface PatientRepository extends JpaRepository<PatientModel, Long>{

    // MÉTODOS ADICIONADOS
    List<PatientModel> findAllByPsychologist(PsychologistModel psychologist);
    Optional<PatientModel> findByIdAndPsychologist(Long id, PsychologistModel psychologist);
}

// ****** FIM DA MODIFICAÇÃO ******
