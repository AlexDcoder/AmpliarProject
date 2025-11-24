package com.example.ampliar.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping; // Mantenha este
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ampliar.dto.settings.UserSettingsDTO;
import com.example.ampliar.dto.settings.UserSettingsUpdateDTO;
import com.example.ampliar.service.UserSettingsService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/settings")
@Slf4j
public class SettingsController {

    private final UserSettingsService userSettingsService;

    public SettingsController(UserSettingsService userSettingsService) {
        this.userSettingsService = userSettingsService;
    }

    // Rota corrigida (sem ID)
    @GetMapping
    public ResponseEntity<UserSettingsDTO> getSettings() {
        log.debug("Recebida requisição GET /settings");
        // O serviço agora descobre o usuário
        UserSettingsDTO settings = userSettingsService.getSettings();
        return ResponseEntity.ok(settings);
    }

    // Rota corrigida (sem ID)
    @PutMapping
    public ResponseEntity<UserSettingsDTO> updateSettings(
            @Valid @RequestBody UserSettingsUpdateDTO dto
    ) {
        log.info("Recebida requisição PUT /settings");
        // O serviço agora descobre o usuário
        UserSettingsDTO updated = userSettingsService.updateSettings(dto);
        return ResponseEntity.ok(updated);
    }
}
