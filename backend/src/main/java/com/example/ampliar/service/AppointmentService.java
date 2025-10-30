package com.example.ampliar.service;

import com.example.ampliar.dto.appointment.AppointmentDTO;
import com.example.ampliar.dto.appointment.AppointmentCreateDTO;
import com.example.ampliar.dto.appointment.AppointmentUpdateDTO;
import com.example.ampliar.mapper.AppointmentDTOMapper;
import com.example.ampliar.model.AppointmentModel;
import com.example.ampliar.model.PatientModel;
import com.example.ampliar.model.PaymentModel;
import com.example.ampliar.model.PsychologistModel;
import com.example.ampliar.repository.AppointmentRepository;
import com.example.ampliar.repository.PatientRepository;
import com.example.ampliar.repository.PaymentRepository;
import com.example.ampliar.repository.PsychologistRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PsychologistRepository psychologistRepository;
    private final PatientRepository patientRepository;
    private final PaymentRepository paymentRepository;
    private final AppointmentDTOMapper mapper;

    public AppointmentDTO createAppointment(AppointmentCreateDTO dto) {
        // 1) psicólogo
        PsychologistModel psych = psychologistRepository.findById(dto.psychologistId())
                .orElseThrow(() -> new EntityNotFoundException("Psicólogo não encontrado"));

        // 2) pacientes
        List<PatientModel> patients = patientRepository.findAllById(dto.patientIds());
        if (patients.size() != dto.patientIds().size()) {
            throw new EntityNotFoundException("Há paciente(s) inexistente(s) no payload");
        }

        // 3) valida conflitos
        validatePsychologistAvailability(dto.appointmentDate(), psych.getId());
        for (PatientModel p : patients) {
            validatePatientAvailability(dto.appointmentDate(), p.getId());
        }

        // 4) pagamento (vem por ID no DTO)
        PaymentModel payment = paymentRepository.findById(dto.paymentId())
                .orElseThrow(() -> new EntityNotFoundException("Pagamento não encontrado"));

        // 5) persiste appointment
        AppointmentModel model = new AppointmentModel();
        model.setAppointmentDate(dto.appointmentDate());
        model.setPsychologist(psych);
        model.setPatients(patients);
        model.setPayment(payment);

        model = appointmentRepository.save(model);
        return mapper.apply(model);
    }

    public AppointmentDTO updateAppointment(Long id, AppointmentUpdateDTO dto) {
        AppointmentModel model = appointmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado"));

        // appointmentDate
        if (dto.appointmentDate() != null) {
            validatePsychologistAvailability(dto.appointmentDate(), model.getPsychologist().getId());
            for (PatientModel p : model.getPatients()) {
                validatePatientAvailability(dto.appointmentDate(), p.getId());
            }
            model.setAppointmentDate(dto.appointmentDate());
        }

        // psychologistId (opcional)
        if (dto.psychologistId() != null && !dto.psychologistId().equals(model.getPsychologist().getId())) {
            PsychologistModel psych = psychologistRepository.findById(dto.psychologistId())
                    .orElseThrow(() -> new EntityNotFoundException("Psicólogo não encontrado"));
            // revalida conflito com a nova agenda
            validatePsychologistAvailability(model.getAppointmentDate(), psych.getId());
            model.setPsychologist(psych);
        }

        // patientIds (opcional)
        if (dto.patientIds() != null && !dto.patientIds().isEmpty()) {
            List<PatientModel> patients = patientRepository.findAllById(dto.patientIds());
            if (patients.size() != dto.patientIds().size()) {
                throw new EntityNotFoundException("Há paciente(s) inexistente(s) no payload");
            }
            for (PatientModel p : patients) {
                validatePatientAvailability(model.getAppointmentDate(), p.getId());
            }
            model.setPatients(patients);
        }

        // paymentId (opcional)
        if (dto.paymentId() != null) {
            PaymentModel payment = paymentRepository.findById(dto.paymentId())
                    .orElseThrow(() -> new EntityNotFoundException("Pagamento não encontrado"));
            model.setPayment(payment);
        }

        model = appointmentRepository.save(model);
        return mapper.apply(model);
    }

    /* ====== os 3 que o controller chama ====== */

    @Transactional
    public void deleteAppointment(Long id) {
        if (!appointmentRepository.existsById(id)) {
            throw new EntityNotFoundException("Agendamento não encontrado");
        }
        appointmentRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public AppointmentDTO getAppointmentById(Long id) {
        AppointmentModel model = appointmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado"));
        return mapper.apply(model);
    }

    @Transactional(readOnly = true)
    public List<AppointmentDTO> getAllAppointments() {
        return appointmentRepository.findAll()
                .stream()
                .map(mapper)          // <- mapper implementa Function<AppointmentModel, AppointmentDTO>
                .collect(Collectors.toList());
    }

    /* ====== validações ====== */

    private void validatePsychologistAvailability(LocalDateTime date, Long psychologistId) {
        boolean conflict = appointmentRepository
                .existsByAppointmentDateAndPsychologistId(date, psychologistId);
        if (conflict) {
            throw new IllegalStateException("O psicólogo já tem um agendamento nesse horário");
        }
    }

    private void validatePatientAvailability(LocalDateTime date, Long patientId) {
        boolean conflict = appointmentRepository
                .existsByAppointmentDateAndPatients_Id(date, patientId);
        if (conflict) {
            throw new IllegalStateException("O paciente já tem um agendamento nesse horário");
        }
    }
}
