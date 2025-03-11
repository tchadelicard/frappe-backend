package fr.imt_atlantique.frappe.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import fr.imt_atlantique.frappe.exceptions.UserAlreadyExistsException;
import fr.imt_atlantique.frappe.repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void ensureUserEmailDoesNotExist(String email) {
        userRepository.findByEmail(email)
                .ifPresent(user -> {
                    throw new UserAlreadyExistsException("User with email " + email + " already exists");
                });
    }

    public void ensureUserUsernameDoesNotExist(String username) {
        userRepository.findByUsername(username)
                .ifPresent(user -> {
                    throw new UserAlreadyExistsException("User with username " + username + " already exists");
                });
    }

    public String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
