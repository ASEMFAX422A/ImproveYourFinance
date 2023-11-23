package org.pdf.finanzverwaltung.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.pdf.finanzverwaltung.AppConfiguration;
import org.pdf.finanzverwaltung.constants.AuthMessageConstants;
import org.pdf.finanzverwaltung.dto.MessageDto;
import org.pdf.finanzverwaltung.dto.RegistrationRequest;
import org.pdf.finanzverwaltung.dto.UserDTO;
import org.pdf.finanzverwaltung.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RegistrationControllerTest {

    @InjectMocks
    private RegistrationController registrationController;

    @Mock
    private UserService userService;

    @Mock
    private AppConfiguration config;

    private RegistrationRequest registrationRequest = new RegistrationRequest("ValidUsername", "P@ssw0rd#");

    @Test
    public void testSuccessfulRegistration() {
        when(userService.addUser(any(UserDTO.class))).thenReturn(true);

        ResponseEntity<MessageDto> response = registrationController.register(registrationRequest);

        assertEquals(AuthMessageConstants.USER_REGISTERED, response.getBody().message);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUserAlreadyExists() {
        when(userService.addUser(any(UserDTO.class))).thenReturn(false);

        ResponseEntity<MessageDto> response = registrationController.register(registrationRequest);

        assertEquals(AuthMessageConstants.USER_ALREADY_EXISTS, response.getBody().message);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testUsernameTooShort() {
        when(config.getUsernameMinLength()).thenReturn(30);

        ResponseEntity<MessageDto> response = registrationController.register(registrationRequest);

        assertEquals(AuthMessageConstants.USERNAME_TOO_SHORT, response.getBody().message);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testPasswordTooShort() {
        when(config.getPasswordMinLength()).thenReturn(30);

        ResponseEntity<MessageDto> response = registrationController.register(registrationRequest);

        assertEquals(AuthMessageConstants.PASSWORD_TOO_SHORT, response.getBody().message);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testPasswordMissingLowercase() {
        when(config.getPasswordMinLowerCharacters()).thenReturn(30);

        ResponseEntity<MessageDto> response = registrationController.register(registrationRequest);

        assertEquals(AuthMessageConstants.PASSWORD_MISSING_LOWER, response.getBody().message);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testPasswordMissingUppercase() {
        when(config.getPasswordMinUpperCharacters()).thenReturn(30);

        ResponseEntity<MessageDto> response = registrationController.register(registrationRequest);

        assertEquals(AuthMessageConstants.PASSWORD_MISSING_UPPER, response.getBody().message);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testPasswordNotEnoughNumbers() {
        when(config.getPasswordMinNumbers()).thenReturn(30);

        ResponseEntity<MessageDto> response = registrationController.register(registrationRequest);

        assertEquals(AuthMessageConstants.PASSWORD_NOT_ENOUGH_NUMBERS, response.getBody().message);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testPasswordNotEnoughSpecialCharacters() {
        when(config.getPasswordMinSpecialCharacters()).thenReturn(30);

        ResponseEntity<MessageDto> response = registrationController.register(registrationRequest);

        assertEquals(AuthMessageConstants.PASSWORD_NOT_ENOUGH_SPECIAL_CHARACTERS, response.getBody().message);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
