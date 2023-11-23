package org.pdf.finanzverwaltung.controllers;

import org.pdf.finanzverwaltung.AppConfiguration;
import org.pdf.finanzverwaltung.dto.SettingsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/settings")
public class SettingsController {

    @Autowired
    private AppConfiguration config;

    public SettingsController() {
    }

    @GetMapping
    public ResponseEntity<SettingsDto> getSettings() {
        return ResponseEntity.ok(config.getSettings());
    }
}