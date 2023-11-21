package org.pdf.finanzverwaltung.controllers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pdf.finanzverwaltung.AppConfiguration;
import org.pdf.finanzverwaltung.constants.AuthMessageConstants;
import org.pdf.finanzverwaltung.dto.MessageDto;
import org.pdf.finanzverwaltung.dto.RegistrationRequest;
import org.pdf.finanzverwaltung.dto.User;
import org.pdf.finanzverwaltung.models.UserRole;
import org.pdf.finanzverwaltung.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/auth/registration")
public class RegistrationController {
    private UserService userService;

    private AppConfiguration config;

    private Pattern numberPattern = Pattern.compile("\\d");
    private Pattern specialCharsPattern = Pattern.compile("[^a-zA-Z0-9]");
    private Pattern minLowerCase = Pattern.compile("[a-z]");
    private Pattern minUpperCase = Pattern.compile("[A-Z]");

    public RegistrationController(UserService userService, AppConfiguration config) {
        this.userService = userService;
        this.config = config;
    }

    @PostMapping
    public ResponseEntity<MessageDto> register(@RequestBody RegistrationRequest request) {
        if (request.getUsername().length() < config.getUsernameMinLength())
            return MessageDto.createResponse(HttpStatus.BAD_REQUEST, AuthMessageConstants.USERNAME_TOO_SHORT);

        if (request.getPassword().length() < config.getPasswordMinLength())
            return MessageDto.createResponse(HttpStatus.BAD_REQUEST, AuthMessageConstants.PASSWORD_TOO_SHORT);

        if (!minAmountPatternCheck(minUpperCase, request.getPassword(), config.getPasswordMinUpperCharacters()))
            return MessageDto.createResponse(HttpStatus.BAD_REQUEST, AuthMessageConstants.PASSWORD_MISSING_UPPER);

        if (!minAmountPatternCheck(minLowerCase, request.getPassword(), config.getPasswordMinLowerCharacters()))
            return MessageDto.createResponse(HttpStatus.BAD_REQUEST, AuthMessageConstants.PASSWORD_MISSING_LOWER);

        if (!minAmountPatternCheck(numberPattern, request.getPassword(), config.getPasswordMinNumbers()))
            return MessageDto.createResponse(HttpStatus.BAD_REQUEST, AuthMessageConstants.PASSWORD_NOT_ENOUGH_NUMBERS);

        if (!minAmountPatternCheck(specialCharsPattern, request.getPassword(), config.getPasswordMinSpecialCharacters()))
            return MessageDto.createResponse(HttpStatus.BAD_REQUEST, AuthMessageConstants.PASSWORD_NOT_ENOUGH_SPECIAL_CHARACTERS);


        final boolean added = userService
                .addUser(new User(request.getUsername(), request.getPassword(), UserRole.USER));

        if (added)
            return MessageDto.createResponse(HttpStatus.OK, AuthMessageConstants.USER_REGISTERED);
        else
            return MessageDto.createResponse(HttpStatus.BAD_REQUEST, AuthMessageConstants.USER_ALREADY_EXISTS);
    }

    private boolean minAmountPatternCheck(Pattern pattern, String input, int minAmount) {
        Matcher matcher = pattern.matcher(input);

        int count = 0;
        while (count < minAmount && matcher.find()) {
            count++;
        }

        return count >= minAmount;
    }
}