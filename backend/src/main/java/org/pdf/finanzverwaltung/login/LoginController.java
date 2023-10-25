package org.pdf.finanzverwaltung.login;

import org.pdf.finanzverwaltung.security.JwtGenerator;
import org.pdf.finanzverwaltung.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/login")
public class LoginController {
    private final AuthenticationManager authManager;
    private final JwtGenerator jwtGenerator;
    private final UserService userService;

    public LoginController(AuthenticationManager authManager, JwtGenerator jwtGenerator, UserService userService) {
        this.authManager = authManager;
        this.jwtGenerator = jwtGenerator;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        UserDetails user = userService.loadUserByUsername(request.getUsername());
        if (user == null) {
            return ResponseEntity.status(400).body("Unknown user");
        }

        Authentication auth = authManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);

        return ResponseEntity.ok("Bearer " + jwtGenerator.generateToken(auth));
    }
}
