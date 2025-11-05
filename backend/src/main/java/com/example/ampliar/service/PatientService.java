package com.example.ampliar.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final PsychologistRepository psychologistRepository; // ADICIONADO
    private final PatientDTOMapper patientDTOMapper;

    @Autowired
    public PatientService(
            PatientRepository patientRepository,
            LegalGuardianRepository legalGuardianRepository,
            PsychologistRepository psychologistRepository, // ADICIONADO
            PatientDTOMapper patientDTOMapper
    ) {
        this.patientRepository = patientRepository;
        this.legalGuardianRepository = legalGuardianRepository;
        this.psychologistRepository = psychologistRepository; // ADICIONADO
        this.patientDTOMapper = patientDTOMapper;
    }

    // MÉTODO ADICIONADO: Helper para buscar o usuário logado
    private PsychologistModel getAuthenticatedPsychologist() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.debug("Buscando psicólogo autenticado por email: {}", email);
        return psychologistRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("Psicólogo não encontrado no contexto de segurança: {}", email);
                    return new UsernameNotFoundException("Psicólogo não encontrado");
                });
    }

    @Transactional
    public PatientDTO createPatient(PatientCreateDTO dto) {
        log.info("Criando paciente: {}", dto.fullName());

        // ADICIONADO: Buscar o "dono" do paciente
        PsychologistModel authPsychologist = getAuthenticatedPsychologist();

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
                    dto.notes()
            );

            // ADICIONADO: Definir o "dono"
            patient.setPsychologist(authPsychologist);

            PatientModel savedPatient = patientRepository.save(patient);

            guardians.forEach(g -> {
                if (g.getPatients() == null || !g.getPatients().contains(savedPatient)) {
                    g.getPatients().add(savedPatient);
                }
            });

            PatientDTO result = patientDTOMapper.apply(savedPatient);
            log.info("Paciente criado com sucesso ID: {} para o psicólogo ID: {}",
                    result.id(), authPsychologist.getId());
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

        // ADICIONADO: Buscar o "dono"
        PsychologistModel authPsychologist = getAuthenticatedPsychologist();

        try {
            // ATUALIZADO: Busca segura
            PatientModel existing = patientRepository.findByIdAndPsychologistId(id, authPsychologist.getId())
                    .orElseThrow(() -> {
                        log.error("Paciente não encontrado para atualização ID: {} (ou não pertence ao psicólogo ID: {})", id, authPsychologist.getId());
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

        // ADICIONADO: Buscar o "dono"
        PsychologistModel authPsychologist = getAuthenticatedPsychologist();

        try {
            // ATUALIZADO: Busca segura
            PatientModel patient = patientRepository.findByIdAndPsychologistId(id, authPsychologist.getId())
                .orElseThrow(() -> {
                    log.warn("Tentativa de excluir paciente inexistente ID: {} (ou não pertence ao psicólogo ID: {})", id, authPsychologist.getId());
                    return new EntityNotFoundException("Paciente não encontrado");
                });

            patientRepository.delete(patient);
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

        // ADICIONADO: Buscar o "dono"
        PsychologistModel authPsychologist = getAuthenticatedPsychologist();

        try {
            // ATUALIZADO: Busca segura
            PatientModel patient = patientRepository.findByIdAndPsychologistId(id, authPsychologist.getId())
                    .orElseThrow(() -> {
                        log.warn("Paciente não encontrado ID: {} (ou não pertence ao psicólogo ID: {})", id, authPsychologist.getId());
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

        // ADICIONADO: Buscar o "dono"
        PsychologistModel authPsychologist = getAuthenticatedPsychologist();

        try {
            // ATUALIZADO: Busca segura
            List<PatientDTO> result = patientRepository.findByPsychologistId(authPsychologist.getId())
                    .stream()
                    .map(patientDTOMapper)
                    .toList();
            log.debug("Encontrados {} pacientes para o psicólogo ID: {}", result.size(), authPsychologist.getId());
            return result;
        } catch (Exception e) {
            log.error("Erro ao buscar todos os pacientes", e);
            throw new RuntimeException("Erro interno ao buscar pacientes", e);
        }
    }
}
