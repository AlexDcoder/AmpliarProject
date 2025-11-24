package com.example.ampliar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder; // Importe
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ampliar.dto.settings.UserSettingsDTO;
import com.example.ampliar.dto.settings.UserSettingsUpdateDTO;
import com.example.ampliar.mapper.UserSettingsMapper;
import com.example.ampliar.model.PsychologistModel;
import com.example.ampliar.model.UserSettingsModel;
import com.example.ampliar.repository.PsychologistRepository;
import com.example.ampliar.repository.UserSettingsRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserSettingsService {

    private final UserSettingsRepository userSettingsRepository;
    private final PsychologistRepository psychologistRepository;
    private final UserSettingsMapper mapper;

    @Autowired
    public UserSettingsService(
            UserSettingsRepository userSettingsRepository,
            PsychologistRepository psychologistRepository,
            UserSettingsMapper mapper
    ) {
        this.userSettingsRepository = userSettingsRepository;
        this.psychologistRepository = psychologistRepository;
        this.mapper = mapper;
    }

    // Helper para buscar o usuário logado
    private PsychologistModel getAuthenticatedPsychologist() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        // Use findByEmail, que é o método que acabamos de adicionar
        return psychologistRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("Psicólogo não encontrado com email: " + username));
    }

    @Transactional // Removido (readOnly = true)
    public UserSettingsDTO getSettings() {
        // Busca o psicólogo pelo token de segurança
        PsychologistModel psychologist = getAuthenticatedPsychologist();

        log.debug("Buscando configurações para psicólogo ID: {}", psychologist.getId());

        UserSettingsModel settings = userSettingsRepository.findByPsychologistId(psychologist.getId())
                .orElseGet(() -> createDefaultSettings(psychologist)); // Passa o objeto
        return mapper.apply(settings);
    }

    @Transactional
    public UserSettingsDTO updateSettings(UserSettingsUpdateDTO dto) {
        // Busca o psicólogo pelo token de segurança
        PsychologistModel psychologist = getAuthenticatedPsychologist();

        log.info("Atualizando configurações para psicólogo ID: {}", psychologist.getId());

        UserSettingsModel settings = userSettingsRepository.findByPsychologistId(psychologist.getId())
                .orElseGet(() -> createDefaultSettings(psychologist)); // Passa o objeto

        if (dto.emailReminders() != null) {
            settings.setEmailReminders(dto.emailReminders());
        }
        if (dto.smsReminders() != null) {
            settings.setSmsReminders(dto.smsReminders());
        }
        if (dto.appointmentConfirmations() != null) {
            settings.setAppointmentConfirmations(dto.appointmentConfirmations());
        }
        if (dto.paymentReminders() != null) {
            settings.setPaymentReminders(dto.paymentReminders());
        }
        if (dto.preferredTheme() != null) {
            settings.setPreferredTheme(dto.preferredTheme());
        }
        if (dto.language() != null) {
            settings.setLanguage(dto.language());
        }
        if (dto.autoBackup() != null) {
            settings.setAutoBackup(dto.autoBackup());
        }
        if (dto.sessionTimeoutMinutes() != null) {
            settings.setSessionTimeoutMinutes(dto.sessionTimeoutMinutes());
        }
        if (dto.defaultAppointmentDuration() != null) {
            settings.setDefaultAppointmentDuration(dto.defaultAppointmentDuration());
        }
        if (dto.twoFactorAuth() != null) {
            settings.setTwoFactorAuth(dto.twoFactorAuth());
        }
        if (dto.passwordExpiryDays() != null) {
            settings.setPasswordExpiryDays(dto.passwordExpiryDays());
        }

        UserSettingsModel saved = userSettingsRepository.save(settings);
        log.info("Configurações atualizadas para psicólogo ID: {}", psychologist.getId());
        return mapper.apply(saved);
    }

    // Modificado para receber o objeto PsychologistModel
    private UserSettingsModel createDefaultSettings(PsychologistModel psychologist) {
        log.debug("Criando configurações padrão para psicólogo ID: {}", psychologist.getId());
        // Não precisamos buscar o psicólogo, já o temos
        UserSettingsModel settings = new UserSettingsModel(psychologist);
        UserSettingsModel saved = userSettingsRepository.save(settings);
        log.info("Configurações padrão criadas para psicólogo ID: {}", psychologist.getId());
        return saved;
    }
}
