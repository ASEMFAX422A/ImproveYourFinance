package org.pdf.finanzverwaltung.services;

import org.pdf.finanzverwaltung.models.User;
import org.pdf.finanzverwaltung.repos.user.DUser;
import org.pdf.finanzverwaltung.repos.user.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepo userRepo;

    public UserService() {
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("user: " + username + " not found"));
    }

    /**
     *
     * @return True only when a user was added to the repo
     */
    public boolean addUser(User user) {
        boolean userExists = userRepo.findByUsername(user.getUsername()).isPresent();
        if (userExists) {
            return false;
        }

        final String password = passwordEncoder.encode(user.getPassword());
        userRepo.save(new DUser(user.getUsername(), password, user.getUserRole()));

        return true;
    }
}
