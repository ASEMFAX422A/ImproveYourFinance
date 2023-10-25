package org.pdf.finanzverwaltung.registration;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/auth/registration")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public ResponseEntity<String> register(@RequestBody RegistrationRequest request) {
        // TODO Check username/password
        final boolean registered = registrationService.register(request);
        if (registered)
            return ResponseEntity.ok("user registered");
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("user could not be registered");
    }

    @GetMapping(path = "/test")
    public ResponseEntity<String> nothing() {
        return ResponseEntity.ok("Nothing here...?");
    }
}
