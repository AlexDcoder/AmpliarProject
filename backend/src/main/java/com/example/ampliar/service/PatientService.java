package com.example.ampliar.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ampliar.dto.patient.PatientCreateDTO;
import com.example.ampliar.dto.patient.PatientDTO;
import com.example.ampliar.dto.patient.PatientUpdateDTO;
import com.example.ampliar.mapper.PatientDTOMapper;
import com.example.ampliar.model.LegalGuardianModel;
import com.example.ampliar.model.PatientModel;
import com.example.ampliar.model.PsychologistModel;
import com.example.ampliar.repository.LegalGuardianRepository;
import com.example.ampliar.repository.PatientRepository;
import com.example.ampliar.repository.PsychologistRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PatientService {

    private final PatientRepository patientRepository;
    private final LegalGuardianRepository legalGuardianRepository;
    private final PatientDTOMapper patientDTOMapper;
    private final PsychologistRepository psychologistRepository;

    @Autowired
    public PatientService(
            PatientRepository patientRepository,
            LegalGuardianRepository legalGuardianRepository,
            PatientDTOMapper patientDTOMapper,
            PsychologistRepository psychologistRepository
    ) {
        this.patientRepository = patientRepository;
        this.legalGuardianRepository = legalGuardianRepository;
        this.patientDTOMapper = patientDTOMapper;
        this.psychologistRepository = psychologistRepository;
    }

    private PsychologistModel getAuthenticatedPsychologist() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return psychologistRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("Psicólogo não encontrado com email: " + username));
    }

    @Transactional
    public PatientDTO createPatient(PatientCreateDTO dto) {
        log.info("Criando paciente: {}", dto.fullName());

        PsychologistModel psychologist = getAuthenticatedPsychologist();

        try {
            List<LegalGuardianModel> guardians = (dto.legalGuardianIds() == null || dto.legalGuardianIds().isEmpty())
                    ? List.of()
                    : legalGuardianRepository.findAllById(dto.legalGuardianIds());

            if (dto.legalGuardianIds() != null && !dto.legalGuardianIds().isEmpty() &&
                    guardians.size() != dto.legalGuardianIds().size()) {
                log.warn("Responsáveis legais não encontrados. Esperados: {}, Encontrados: {}",
                        dto.legalGuardianIds().size(), guardians.size());
                throw new EntityNotFoundException("Um ou mais responsáveis legais não foram encontrados");
            }

            PatientModel patient = new PatientModel(
                    dto.birthDate(),
                    guardians,
                    dto.fullName(),
                    dto.cpf(),
                    dto.phoneNumber(),
                    dto.email(),
                    dto.address(),
                    dto.notes(),
                    psychologist
            );

            PatientModel savedPatient = patientRepository.save(patient);

            guardians.forEach(g -> {
                if (g.getPatients() == null || !g.getPatients().contains(savedPatient)) {
                    g.getPatients().add(savedPatient);
                }
            });

            PatientDTO result = patientDTOMapper.apply(savedPatient);
            log.info("Paciente criado com sucesso ID: {} com {} responsáveis",
                    result.id(), guardians.size());
            return result;

        } catch (EntityNotFoundException e) {
            log.error("Erro ao criar paciente - recurso não encontrado: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao criar paciente", e);
            throw new RuntimeException("Erro interno ao criar paciente", e);
        }
    }

    @Transactional
    public PatientDTO updatePatient(Long id, PatientUpdateDTO dto) {
        log.info("Atualizando paciente ID: {}", id);

        PsychologistModel psychologist = getAuthenticatedPsychologist();

        try {
            PatientModel existing = patientRepository.findByIdAndPsychologist(id, psychologist)
                    .orElseThrow(() -> {
                        log.error("Paciente não encontrado para atualização ID: {}", id);
                        return new EntityNotFoundException("Paciente não encontrado");
                    });

            if (dto.fullName() != null) {
                existing.setFullName(dto.fullName());
                log.debug("Nome do paciente atualizado");
            }
            if (dto.cpf() != null) {
                existing.setCpf(dto.cpf());
                log.debug("CPF do paciente atualizado");
            }
            if (dto.phoneNumber() != null) {
                existing.setPhoneNumber(dto.phoneNumber());
                log.debug("Telefone do paciente atualizado");
            }
            if (dto.birthDate() != null) {
                existing.setBirthDate(dto.birthDate());
                log.debug("Data de nascimento do paciente atualizada");
            }

            if (dto.email() != null) {
                existing.setEmail(dto.email());
                log.debug("Email do paciente atualizado");
            }
            if (dto.address() != null) {
                existing.setAddress(dto.address());
                log.debug("Endereço do paciente atualizado");
            }
            if (dto.notes() != null) {
                existing.setNotes(dto.notes());
                log.debug("Notas do paciente atualizadas");
            }

            if (dto.legalGuardianIds() != null) {
                List<LegalGuardianModel> guardians = legalGuardianRepository.findAllById(dto.legalGuardianIds());

                if (guardians.size() != dto.legalGuardianIds().size()) {
                    log.warn("Responsáveis legais não encontrados na atualização. Esperados: {}, Encontrados: {}",
                            dto.legalGuardianIds().size(), guardians.size());
                    throw new EntityNotFoundException("Um ou mais responsáveis legais não foram encontrados");
                }

                existing.getLegalGuardians().forEach(g -> g.getPatients().remove(existing));

                existing.setLegalGuardians(guardians);
                guardians.forEach(g -> {
                    if (g.getPatients() == null || !g.getPatients().contains(existing)) {
                        g.getPatients().add(existing);
                    }
                });
                log.debug("{} responsáveis legais atualizados para o paciente", guardians.size());
            }

            PatientDTO result = patientDTOMapper.apply(patientRepository.save(existing));
            log.info("Paciente atualizado com sucesso ID: {}", id);
            return result;

        } catch (EntityNotFoundException e) {
            log.error("Paciente não encontrado para atualização ID: {}", id);
            throw e;
        } catch (Exception e) {
            log.error("Erro ao atualizar paciente ID: {}", id, e);
            throw new RuntimeException("Erro interno ao atualizar paciente", e);
        }
    }

    @Transactional
    public void deletePatient(Long id) {
        log.info("Excluindo paciente ID: {}", id);

        PsychologistModel psychologist = getAuthenticatedPsychologist();

        try {
            PatientModel patient = patientRepository.findByIdAndPsychologist(id, psychologist)
                    .orElseThrow(() -> {
                        log.warn("Tentativa de excluir paciente inexistente ID: {}", id);
                        return new EntityNotFoundException("Paciente não encontrado");
                    });

            patientRepository.deleteById(patient.getId());
            log.info("Paciente excluído com sucesso ID: {}", id);

        } catch (EntityNotFoundException e) {
            log.warn("Paciente não encontrado para exclusão ID: {}", id);
            throw e;
        } catch (Exception e) {
            log.error("Erro ao excluir paciente ID: {}", id, e);
            throw new RuntimeException("Erro interno ao excluir paciente", e);
        }
    }

    @Transactional(readOnly = true)
    public PatientDTO getPatientById(Long id) {
        log.debug("Buscando paciente por ID: {}", id);

        PsychologistModel psychologist = getAuthenticatedPsychologist();

        try {
            PatientModel patient = patientRepository.findByIdAndPsychologist(id, psychologist)
                    .orElseThrow(() -> {
                        log.warn("Paciente não encontrado ID: {}", id);
                        return new EntityNotFoundException("Paciente não encontrado");
                    });

            PatientDTO result = patientDTOMapper.apply(patient);
            log.debug("Paciente encontrado ID: {}", id);
            return result;

        } catch (EntityNotFoundException e) {
            log.warn("Paciente não encontrado na consulta ID: {}", id);
            throw e;
        } catch (Exception e) {
            log.error("Erro ao buscar paciente ID: {}", id, e);
            throw new RuntimeException("Erro interno ao buscar paciente", e);
        }
    }

    @Transactional(readOnly = true)
    public List<PatientDTO> getAllPatients() {
        log.debug("Buscando todos os pacientes");

        PsychologistModel psychologist = getAuthenticatedPsychologist();

        try {
            List<PatientDTO> result = patientRepository.findAllByPsychologist(psychologist)
                    .stream()
                    .map(patientDTOMapper)
                    .toList();
            log.debug("Encontrados {} pacientes", result.size());
            return result;
        } catch (Exception e) {
            log.error("Erro ao buscar todos os pacientes", e);
            throw new RuntimeException("Erro interno ao buscar pacientes", e);
        }
    }
}
