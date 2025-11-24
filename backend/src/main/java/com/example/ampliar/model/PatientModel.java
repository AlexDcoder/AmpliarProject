package com.example.ampliar.model;

// A importação de AppointmentStatus foi removida daqui
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.ampliar.validation.constraints.BirthDate;
import com.fasterxml.jackson.annotation.JsonBackReference; // IMPORTAR
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne; // IMPORTAR
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "patient")
public class PatientModel extends PersonAbstract {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Long id;

    @BirthDate
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Email
    @Column(unique = true)
    private String email;

    @Column
    private String address;

    @Column(columnDefinition = "TEXT")
    private String notes;


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "patient_guardians",
            joinColumns = @JoinColumn(name = "patient_id"),
            inverseJoinColumns = @JoinColumn(name = "guardian_id")
    )
    @JsonManagedReference
    private List<LegalGuardianModel> legalGuardians = new ArrayList<>();

    @ManyToMany(mappedBy = "patients", fetch = FetchType.LAZY)
    private List<AppointmentModel> appointments = new ArrayList<>();

    // ****** INÍCIO DA MODIFICAÇÃO ******

    // CAMPO ADICIONADO
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "psychologist_id", nullable = false)
    @JsonBackReference
    private PsychologistModel psychologist;

    // CONSTRUTOR ATUALIZADO
    public PatientModel(LocalDate birthDate, List<LegalGuardianModel> legalGuardians, String fullName, String cpf, String phoneNumber, String email, String address, String notes, PsychologistModel psychologist) {
        super(fullName, cpf, phoneNumber);
        this.birthDate = birthDate;
        this.legalGuardians = legalGuardians;
        this.email = email;
        this.address = address;
        this.notes = notes;
        this.psychologist = psychologist; // LINHA ADICIONADA
    }

    // MÉTODO ADICIONADO
    public void setPsychologist(PsychologistModel psychologist) {
        this.psychologist = psychologist;
    }

    // ****** FIM DA MODIFICAÇÃO ******
}
