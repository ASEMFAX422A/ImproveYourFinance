package org.pdf.finanzverwaltung.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.pdf.finanzverwaltung.constants.AuthMessageConstants;
import org.pdf.finanzverwaltung.dto.LoginRequest;
import org.pdf.finanzverwaltung.dto.MessageDto;
import org.pdf.finanzverwaltung.security.JwtGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LoginControllerTest {

    @Mock
    private AuthenticationManager authManager;

    @Mock
    private JwtGenerator jwtGenerator;

    @InjectMocks
    private LoginController loginController;

    private LoginRequest request = new LoginRequest("ValidUsername", "P@ssw0rd#");

    @Test
    public void testSuccessfulLogin() {
        Authentication authentication = mock(Authentication.class);
        when(authManager.authenticate(any())).thenReturn(authentication);
        when(jwtGenerator.generateToken(authentication)).thenReturn("fakeToken");

        ResponseEntity<MessageDto> response = loginController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("fakeToken", response.getBody().message);
    }

    @Test
    public void testUserNotFound() {
        when(authManager.authenticate(any())).thenThrow(new UsernameNotFoundException("User " + request.getUsername() + " not found"));

        ResponseEntity<MessageDto> response = loginController.login(request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(AuthMessageConstants.USER_NON_EXISTENT, response.getBody().message);
    }

    @Test
    public void testInvalidCredentials() {
        when(authManager.authenticate(any())).thenThrow(new BadCredentialsException("Invalid credentials"));

        ResponseEntity<MessageDto> response = loginController.login(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(AuthMessageConstants.INVALID_CREDENTIALS, response.getBody().message);
    }
}