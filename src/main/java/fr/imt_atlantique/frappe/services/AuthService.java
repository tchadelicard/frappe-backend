package fr.imt_atlantique.frappe.services;

import fr.imt_atlantique.frappe.dtos.*;
import fr.imt_atlantique.frappe.entities.CampusRepository;
import fr.imt_atlantique.frappe.entities.User;
import fr.imt_atlantique.frappe.repositories.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    private final JwtService jwtService;
    private final CampusRepository campusRepository;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JavaMailSender mailSender, JwtService jwtService,
                       CampusRepository campusRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
        this.jwtService = jwtService;
        this.campusRepository = campusRepository;
    }

    @Transactional
    public RegistrationResponse register(RegistrationRequest request) {
        String validationMessage = validateRegistrationRequest(request);
        if (validationMessage != null) {
            return RegistrationResponse.builder()
                    .success(false)
                    .message(validationMessage)
                    .build();
        }

        User user = createUser(request);
        userRepository.save(user);

        try {
            sendVerificationEmail(user);
        } catch (MessagingException e) {
            return RegistrationResponse.builder()
                    .success(false)
                    .message("Error sending verification email.")
                    .build();
        }

        return RegistrationResponse.builder()
                .success(true)
                .message("User registered successfully. Please verify your email.")
                .build();
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isEmpty() || !passwordEncoder.matches(request.getPassword(), userOptional.get().getPassword())) {
            return LoginResponse.builder()
                    .token(null)
                    .message("Invalid email or password.")
                    .build();
        }

        User user = userOptional.get();
        if (!user.isEnabled()) {
            return LoginResponse.builder()
                    .token(null)
                    .message("Account is not verified.")
                    .build();
        }

        String token = jwtService.generateToken(user);
        return LoginResponse.builder()
                .token(token)
                .message("Login successful.")
                .build();
    }

    @Transactional
    public VerifyResponse verifyAccount(String token) {
        Optional<User> userOptional = userRepository.findByValidationCode(token);
        if (userOptional.isEmpty()) {
            return VerifyResponse.builder()
                    .success(false)
                    .message("Invalid or expired validation token.")
                    .build();
        }

        User user = userOptional.get();
        if (user.getValidationCodeExpiry().isBefore(Instant.now())) {
            return VerifyResponse.builder()
                    .success(false)
                    .message("Validation token has expired.")
                    .build();
        }

        enableUserAccount(user);
        return VerifyResponse.builder()
                .success(true)
                .message("Account verified successfully!")
                .build();
    }

    private String validateRegistrationRequest(RegistrationRequest request) {
        if (!isValidEmail(request.getEmail())) {
            return "Invalid email format.";
        }
        if (!isValidPassword(request.getPassword())) {
            return "Password must be at least 8 characters, include an uppercase letter, a lowercase letter, a number, and a special character.";
        }
        if (!isValidPhoneNumber(request.getPhoneNumber())) {
            return "Invalid phone number format.";
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            return "Email is already taken.";
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            return "Username is already taken.";
        }
        if (!campusRepository.existsById(request.getCampusId())) {
            return "Invalid campus ID.";
        }
        return null;
    }

    private User createUser(RegistrationRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setEnabled(false);
        user.setValidationCode(UUID.randomUUID().toString());
        user.setValidationCodeExpiry(Instant.now().plus(Duration.ofHours(1)));
        user.setCampus(campusRepository.findById(request.getCampusId()).orElseThrow());
        return user;
    }

    private void enableUserAccount(User user) {
        user.setEnabled(true);
        user.setValidationCode(null);
        user.setValidationCodeExpiry(null);
        userRepository.save(user);
    }

    private void sendVerificationEmail(User user) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("no-reply@localhost.local");
        helper.setTo(user.getEmail());
        helper.setSubject("Verify Your Account");
        helper.setText("Validation code: " + user.getValidationCode());
        mailSender.send(message);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@imt-atlantique\\.(?:fr|net)$";
        return Pattern.matches(emailRegex, email);
    }

    private boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return Pattern.matches(passwordRegex, password);
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        String phoneRegex = "^[+]?[0-9]{10,15}$";
        return Pattern.matches(phoneRegex, phoneNumber);
    }
}