package com.example.ampliar.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ampliar.model.AppointmentModel;
import com.example.ampliar.model.PatientModel;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentModel, Long> {

    boolean existsByAppointmentDateAndPsychologistId(LocalDateTime date, Long psychologistId);

    boolean existsByAppointmentDateAndPatients_Id(LocalDateTime date, Long patientId);

    Integer countByPatientsContains(PatientModel patient);
}
