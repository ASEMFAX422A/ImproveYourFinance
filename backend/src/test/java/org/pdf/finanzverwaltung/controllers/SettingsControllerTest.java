package org.pdf.finanzverwaltung.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.pdf.finanzverwaltung.AppConfiguration;
import org.pdf.finanzverwaltung.dto.SettingsDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RunWith(MockitoJUnitRunner.class)
public class SettingsControllerTest {

    @Mock
    private AppConfiguration config;

    @InjectMocks
    private SettingsController settingsController;
    
    @Test
    public void testGetSettings() {
        SettingsDto mockSettings = new SettingsDto(1, 2, 3, 4);
        when(config.getSettings()).thenReturn(mockSettings);

        ResponseEntity<SettingsDto> responseEntity = settingsController.getSettings();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockSettings, responseEntity.getBody());
    }
}
