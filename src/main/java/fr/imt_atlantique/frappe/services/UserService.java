package fr.imt_atlantique.frappe.services;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import fr.imt_atlantique.frappe.dtos.LoginRequest;
import fr.imt_atlantique.frappe.entities.User;
import fr.imt_atlantique.frappe.exceptions.UserAlreadyExistsException;
import fr.imt_atlantique.frappe.exceptions.UserNotFoundException;
import fr.imt_atlantique.frappe.exceptions.ValidationCodeNotFoundException;
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

    public User enableUserAccount(User user) {
        user.setEnabled(true);
        user.setValidationCode(null);
        user.setValidationCodeExpiry(null);
        userRepository.save(user);
        return user;
    }

    public User processLoginRequest(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ValidationCodeNotFoundException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ValidationCodeNotFoundException("Invalid email or password");
        }

        if (!user.isEnabled()) {
            throw new ValidationCodeNotFoundException("User account is not enabled");
        }

        return user;
    }

    public void checkUserValidationCode(String validationCode) {
        User user = userRepository.findByValidationCode(validationCode)
                .orElseThrow(() -> new ValidationCodeNotFoundException("Invalid validation code"));
        if (user.getValidationCodeExpiry().isBefore(Instant.now())) {
            throw new ValidationCodeNotFoundException("Validation code has expired");
        }
        enableUserAccount(user);
    }

    public User generateValidationCode(User user) {
        user.setValidationCode(UUID.randomUUID().toString());
        user.setValidationCodeExpiry(Instant.now().plus(Duration.ofMinutes(10)));
        userRepository.save(user);
        return user;
    }

    public User resendValidationCode(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email not found"));
        if (user.isEnabled()) {
            throw new UserAlreadyExistsException("User account is already verified");
        }
        user = generateValidationCode(user);
        return user;
    }

    public void checkIfUserExists(User user) {
        userRepository.findByUsername(user.getUsername())
                .ifPresent(student -> {
                    if (student.isEnabled()) {
                        throw new UserAlreadyExistsException("Username already exists");
                    }
                });
        userRepository.findByEmail(user.getEmail())
                .ifPresent(student -> {
                    if (student.isEnabled()) {
                        throw new UserAlreadyExistsException("Email already exists");
                    }
                });

    }

    public List<String> getUserRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
    }
}
