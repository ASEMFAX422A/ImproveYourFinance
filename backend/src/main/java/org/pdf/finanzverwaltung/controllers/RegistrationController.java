package org.pdf.finanzverwaltung.controllers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pdf.finanzverwaltung.AppConfiguration;
import org.pdf.finanzverwaltung.dto.MessageDto;
import org.pdf.finanzverwaltung.dto.RegistrationRequest;
import org.pdf.finanzverwaltung.dto.User;
import org.pdf.finanzverwaltung.models.UserRole;
import org.pdf.finanzverwaltung.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/auth/registration")
public class RegistrationController {
    private static final Pattern specialCharsPattern = Pattern.compile("[^a-zA-Z0-9]");
    private static final Pattern numberPattern = Pattern.compile("\\d");

    @Autowired
    private UserService userService;

    @Autowired
    private AppConfiguration config;

    public RegistrationController() {
    }

    @PostMapping
    public ResponseEntity<MessageDto> register(@RequestBody RegistrationRequest request) {
        if (request.getUsername().length() < config.getUsernameMinLength())
            return MessageDto.createResponse(HttpStatus.BAD_REQUEST, "Username to short");

        if (request.getPassword().length() < config.getPasswordMinLength())
            return MessageDto.createResponse(HttpStatus.BAD_REQUEST, "Password to short");

        if (!hasLowerUpper(request.getPassword()))
            return MessageDto.createResponse(HttpStatus.BAD_REQUEST, "Password requires lower and upper case a-z");

        if (countDigits(request.getPassword()) < config.getPasswordMinNumbers())
            return MessageDto.createResponse(HttpStatus.BAD_REQUEST, "Password not enough numbers");

        if (countSpecialCharacters(request.getPassword()) < config.getPasswordMinSpecialCharacters())
            return MessageDto.createResponse(HttpStatus.BAD_REQUEST, "Password not enough special characters");

        final boolean added = userService
                .addUser(new User(request.getUsername(), request.getPassword(), UserRole.USER));

        if (added)
            return MessageDto.createResponse(HttpStatus.OK, "User registered");
        else
            return MessageDto.createResponse(HttpStatus.BAD_REQUEST, "User could not be registered");
    }

    public boolean hasLowerUpper(String input) {
        return input.matches(".*[a-z].*") && input.matches(".*[A-Z].*");
    }

    private int countDigits(String input) {
        Matcher matcher = numberPattern.matcher(input);

        int count = 0;
        while (matcher.find()) {
            count++;
        }

        return count;
    }

    private int countSpecialCharacters(String input) {
        Matcher matcher = specialCharsPattern.matcher(input);

        int count = 0;
        while (matcher.find()) {
            count++;
        }

        return count;
    }
}