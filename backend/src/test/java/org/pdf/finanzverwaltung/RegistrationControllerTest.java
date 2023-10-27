package org.pdf.finanzverwaltung;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.pdf.finanzverwaltung.registration.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RegistrationControllerTest {
    @InjectMocks
    private RegistrationController registrationController;

    @Mock
    private RegistrationService registrationService;

    @Test
    public void testRegistrationSuccess() {
        // Arrange
        RegistrationRequest request = new RegistrationRequest("validUsername", "validPassword");
        when(registrationService.register(request)).thenReturn(true);

        // Act
        ResponseEntity<String> response = registrationController.register(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{ \"messsage\": \"user registered\" }", response.getBody());
    }

    @Test
    public void testRegistrationFailure() {
        // Arrange
        RegistrationRequest request = new RegistrationRequest("invalidUsername", "invalidPassword");
        when(registrationService.register(request)).thenReturn(false);

        // Act
        ResponseEntity<String> response = registrationController.register(request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{ \"messsage\": \"user could not be registered\" }", response.getBody());
    }
}
