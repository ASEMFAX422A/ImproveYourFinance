package org.pdf.finanzverwaltung.controllers;

import org.pdf.finanzverwaltung.models.LoginRequest;
import org.pdf.finanzverwaltung.security.JwtGenerator;
import org.pdf.finanzverwaltung.services.UserService;
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
    public ResponseEntity<Response> login(@RequestBody LoginRequest request) {
        UserDetails user = userService.loadUserByUsername(request.getUsername());
        if (user == null) {
            return ResponseEntity.status(400).body(null);
        }

        Authentication auth = authManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);

        return ResponseEntity.ok(new Response(jwtGenerator.generateToken(auth)));
    }

    public class Response {
        private final String token;

        public Response(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }
    }
}
