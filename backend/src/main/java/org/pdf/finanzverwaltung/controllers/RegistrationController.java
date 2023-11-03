package org.pdf.finanzverwaltung.controllers;

import org.pdf.finanzverwaltung.models.RegistrationRequest;
import org.pdf.finanzverwaltung.models.User;
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

    @Autowired
    private UserService userService;

    public RegistrationController() {
    }

    @PostMapping
    public ResponseEntity<Response> register(@RequestBody RegistrationRequest request) {
        // TODO Check username/password
        final boolean added = userService
                .addUser(new User(request.getUsername(), request.getPassword(), UserRole.USER));

        if (added)
            return ResponseEntity.ok(new Response("user registered"));
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response("user could not be registered"));
    }

    public class Response {
        public String message;

        public Response(String message) {
            this.message = message;
        }
    }
}
