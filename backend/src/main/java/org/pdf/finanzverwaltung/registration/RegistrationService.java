package org.pdf.finanzverwaltung.registration;

import org.pdf.finanzverwaltung.user.User;
import org.pdf.finanzverwaltung.user.UserRole;
import org.pdf.finanzverwaltung.user.UserService;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private final UserService userService;

    public RegistrationService(UserService userService) {
        this.userService = userService;
    }

    public boolean register(RegistrationRequest req) {
        return userService.addUser(new User(req.getUsername(), req.getPassword(), UserRole.USER));
    }
}
