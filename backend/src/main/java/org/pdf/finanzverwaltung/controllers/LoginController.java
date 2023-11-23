package org.pdf.finanzverwaltung.controllers;

import org.pdf.finanzverwaltung.constants.AuthMessageConstants;
import org.pdf.finanzverwaltung.constants.GeneralMessageConstants;
import org.pdf.finanzverwaltung.dto.MessageDto;
import org.pdf.finanzverwaltung.dto.LoginRequest;
import org.pdf.finanzverwaltung.security.JwtGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/login")
public class LoginController {
    private final AuthenticationManager authManager;
    private final JwtGenerator jwtGenerator;

    public LoginController(AuthenticationManager authManager, JwtGenerator jwtGenerator) {
        this.authManager = authManager;
        this.jwtGenerator = jwtGenerator;
    }

    @PostMapping
    public ResponseEntity<MessageDto> login(@RequestBody LoginRequest request) {
        try {
            Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(auth);

            return MessageDto.createResponse(HttpStatus.OK, jwtGenerator.generateToken(auth));
        } catch(UsernameNotFoundException e) {
            return MessageDto.createResponse(HttpStatus.NOT_FOUND, AuthMessageConstants.USER_NON_EXISTENT);
        } catch (BadCredentialsException e) {
            return MessageDto.createResponse(HttpStatus.UNAUTHORIZED, AuthMessageConstants.INVALID_CREDENTIALS);
        } catch (Exception e) {
            return MessageDto.createResponse(HttpStatus.INTERNAL_SERVER_ERROR, GeneralMessageConstants.INTERNAL_SERVER_ERROR);
        }
    }
}
