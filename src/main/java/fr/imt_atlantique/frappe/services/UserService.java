package fr.imt_atlantique.frappe.services;

import fr.imt_atlantique.frappe.entities.Campus;
import fr.imt_atlantique.frappe.entities.User;
import fr.imt_atlantique.frappe.entities.validation;
import fr.imt_atlantique.frappe.repositories.CampusRepository;
import fr.imt_atlantique.frappe.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@RequiredArgsConstructor // Use Lombok to generate a constructor for all final fields
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final CampusRepository campusRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final ValidationService validationService;

    public User addUser(User user) {
        Campus campus = user.getCampus();
        if (campus.getId() == null || !campusRepository.existsById(campus.getId())) {
            campus = campusRepository.save(campus); // Save the new Campus entity
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
        if (user.getPassword().length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (!user.getEmail().contains("@")) {
            throw new IllegalArgumentException("Email must be valid");
        }
        if (user.getFirstName() == null || user.getFirstName().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (user.getLastName() == null || user.getLastName().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (user.getPhoneNumber() == null || user.getPhoneNumber().isEmpty()) {
            throw new IllegalArgumentException("Phone number is required");
        }

        userRepository.findByEmail(user.getEmail()).ifPresent(u -> {
            throw new RuntimeException("Email already exists");
        });

        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        user.setCampus(campus);
        user = this.userRepository.save(user);
        this.validationService.register(user);

        return user;
    }

    public void activation(Map<String, String> activation) {
        validation validation = this.validationService.readCode(activation.get("token"));
        if (Instant.now().isAfter(validation.getExpire())) {
            throw new RuntimeException("Validation code expired");
        }

        User userActiv = this.userRepository.findById(validation.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        userActiv.setActive(true);
        this.userRepository.save(userActiv);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      log.info("User with email {} is trying to log in", username);
      User user  = this.userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }

   
}
