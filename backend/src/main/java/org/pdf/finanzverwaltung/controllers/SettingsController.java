package org.pdf.finanzverwaltung.controllers;

import org.pdf.finanzverwaltung.AppConfiguration;
import org.pdf.finanzverwaltung.dto.SettingsDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/settings")
public class SettingsController {

    private AppConfiguration config;

    public SettingsController(AppConfiguration config) {
        this.config = config;
    }

    @GetMapping
    public ResponseEntity<SettingsDto> getSettings() {
        return ResponseEntity.ok(config.getSettings());
    }
}