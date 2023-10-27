package org.pdf.finanzverwaltung.registration;

import org.pdf.finanzverwaltung.user.User;
import org.pdf.finanzverwaltung.user.UserRole;
import org.pdf.finanzverwaltung.user.UserService;
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

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<String> register(@RequestBody RegistrationRequest request) {
        // TODO: Check username/password
        final boolean registered = userService.addUser(new User(request.getUsername(), request.getPassword(), UserRole.USER));
        if (registered)
            return ResponseEntity.ok("{ \"messsage\": \"user registered\" }");
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{ \"messsage\": \"user could not be registered\" }");
    }

    @GetMapping(path = "/test")
    public ResponseEntity<String> nothing() {
        return ResponseEntity.ok("Nothing here...?");
    }
}
