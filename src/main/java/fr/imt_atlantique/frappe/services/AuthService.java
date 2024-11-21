package fr.imt_atlantique.frappe.services;

import fr.imt_atlantique.frappe.dtos.*;
import fr.imt_atlantique.frappe.entities.*;
import fr.imt_atlantique.frappe.repositories.UserRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JavaMailSender mailSender, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
        this.jwtService = jwtService;
    }

    public RegistrationResponse register(RegistrationRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return RegistrationResponse.builder()
                    .success(false)
                    .message("Email already registered.")
                    .build();
        }

        User user = new User();
        user.setUsername(request.getUsername());

        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(false);
        user.setStudent(new Student());
        user.setValidationCode(UUID.randomUUID().toString());

        userRepository.save(user);
        sendVerificationEmail(user);

        return RegistrationResponse.builder()
                .success(true)
                .message("Registration successful. Check your email for verification.")
                .build();
    }

    public void sendVerificationEmail(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Verify Your Account");
        message.setText("Click the link to activate your account: http://localhost:8080/auth/validate?token=" + user.getValidationCode());
        mailSender.send(message);
    }

    public LoginResponse login(LoginRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isEmpty()) {
            throw new RuntimeException("Invalid credentials.");
        }

        User user = userOptional.get();
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials.");
        }

        if (!user.isEnabled()) {
            return LoginResponse.builder()
                    .message("Account not verified.")
                    .token(null)
                    .build();
        }

        String token = jwtService.generateToken(user);
        return LoginResponse.builder()
                .message("Login successful.")
                .token(token)
                .build();
    }

    public String validateAccount(String token) {
        Optional<User> userOptional = userRepository.findByValidationCode(token);

        if (userOptional.isEmpty()) {
            return "Invalid or expired validation code.";
        }

        User user = userOptional.get();
        user.setEnabled(true);
        user.setValidationCode(null);
        userRepository.save(user);

        return "Account verified successfully!";
    }

    public RegistrationResponse addSupervisor(SupervisorRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return RegistrationResponse.builder()
                    .success(false)
                    .message("Email already registered.")
                    .build();
        }

        User supervisor = new User();
        supervisor.setEmail(request.getEmail());
        supervisor.setPassword(passwordEncoder.encode(request.getPassword()));
        supervisor.setEnabled(true);
        supervisor.setSupervisor(new Supervisor());

        userRepository.save(supervisor);

        return RegistrationResponse.builder()
                .success(true)
                .message("Supervisor added successfully.")
                .build();
    }
}