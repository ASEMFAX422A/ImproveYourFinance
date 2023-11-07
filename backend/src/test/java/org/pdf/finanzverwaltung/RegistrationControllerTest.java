package org.pdf.finanzverwaltung;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.pdf.finanzverwaltung.controllers.RegistrationController;
import org.pdf.finanzverwaltung.controllers.RegistrationController.Response;
import org.pdf.finanzverwaltung.models.RegistrationRequest;
import org.pdf.finanzverwaltung.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RegistrationControllerTest {
    @InjectMocks
    private RegistrationController registrationController;

    @Mock
    private UserService userService;

    @Test
    public void testRegistrationSuccess() {
        // Arrange
        RegistrationRequest request = new RegistrationRequest("validUsername", "validPassword");
        when(userService.addUser(any())).thenReturn(true);

        // Act
        ResponseEntity<Response> response = registrationController.register(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testRegistrationFailure() {
        // Arrange
        RegistrationRequest request = new RegistrationRequest("invalidUsername", "invalidPassword");
        when(userService.addUser(any())).thenReturn(false);

        // Act
        ResponseEntity<Response> response = registrationController.register(request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
