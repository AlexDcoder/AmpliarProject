package com.example.ampliar.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "appointment")
@Getter
@NoArgsConstructor
public class AppointmentModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime appointmentDate;

    // n appointments -> 1 psychologist (N:1)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "psychologist_id", nullable = false) // sem unique aqui
    private PsychologistModel psychologist;

    // n appointments <-> n patients (N:N)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "appointment_patients",
            joinColumns = @JoinColumn(name = "appointment_id"),
            inverseJoinColumns = @JoinColumn(name = "patient_id")
    )
    private List<PatientModel> patients = new ArrayList<>();

    // 1:1 com Payment
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payment_id", nullable = false, unique = true)
    private PaymentModel payment;

    public void setAppointmentDate(LocalDateTime appointmentDate) {
        if (appointmentDate == null) {
            throw new IllegalArgumentException("Data do agendamento é obrigatória");
        }
        this.appointmentDate = appointmentDate;
    }

    public void setPsychologist(PsychologistModel psychologist) {
        if (psychologist == null) {
            throw new IllegalArgumentException("Psicólogo é obrigatório");
        }
        this.psychologist = psychologist;
    }

    public void setPatients(List<PatientModel> patients) {
        if (patients == null || patients.isEmpty()) {
            throw new IllegalArgumentException("Informe pelo menos 1 paciente");
        }
        this.patients = patients;
    }

    public void setPayment(PaymentModel payment) {
        if (payment == null) {
            throw new IllegalArgumentException("Pagamento é obrigatório");
        }
        this.payment = payment;
    }
}
