package org.pdf.finanzverwaltung.services;

import java.util.Optional;

import org.pdf.finanzverwaltung.dto.UserDTO;
import org.pdf.finanzverwaltung.models.DUser;
import org.pdf.finanzverwaltung.repos.user.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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

    /**
     * @return True only when the user was added to the repo
     */
    public boolean addUser(UserDTO user) {
        boolean userExists = userRepo.findByUsername(user.getUsername()) != null;
        if (userExists) {
            return false;
        }

        final String password = passwordEncoder.encode(user.getPassword());
        userRepo.save(new DUser(user.getUsername(), password, user.getUserRole()));

        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final DUser user = userRepo.findByUsername(username);
        if (user == null)
            throw new UsernameNotFoundException("user: " + username + " not found");

        return user;
    }

    public UserDTO getById(long id) {
        Optional<DUser> userOpt = userRepo.findById(id);
        if (!userOpt.isPresent())
            return null;

        return dUserToUser(userOpt.get());
    }

    public UserDTO getCurrentUser() {
        Object user = getCurrentDUser();
        if (user == null)
            return null;

        return dUserToUser((DUser) user);
    }

    public DUser getCurrentDUser() {
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user == null)
            return null;

        return (DUser) user;
    }

    public UserDTO dUserToUser(DUser user) {
        if (user == null)
            return null;

        return new UserDTO(user.getId(), user.getUsername(), user.getPassword(), user.getRole());
    }

    public DUser userToDUser(UserDTO user) {
        if (user == null)
            return null;

        final DUser currentUser = getCurrentDUser();
        if (currentUser.getId() == user.getId())
            return currentUser;

        Optional<DUser> userOpt = userRepo.findById(user.getId());
        if (userOpt.isPresent())
            return userOpt.get();

        return new DUser(user.getUsername(), user.getPassword(), user.getUserRole());
    }
}
