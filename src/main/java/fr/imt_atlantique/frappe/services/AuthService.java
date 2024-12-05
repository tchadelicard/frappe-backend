package fr.imt_atlantique.frappe.services;

import fr.imt_atlantique.frappe.dtos.*;
import fr.imt_atlantique.frappe.entities.Student;
import fr.imt_atlantique.frappe.repositories.StudentRepository;
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
    private final StudentRepository studentRepository;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JavaMailSender mailSender, JwtService jwtService,
                       StudentRepository studentRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
        this.jwtService = jwtService;
        this.studentRepository = studentRepository;
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

        Student student = createStudent(request);
        studentRepository.save(student);

        try {
            sendVerificationEmail(student);
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
        if (userRepository.existsByEmail(request.getEmail())) {
            return "Email is already taken.";
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            return "Username is already taken.";
        }
        return null;
    }

    private Student createStudent(RegistrationRequest request) {
        Student student = new Student();
        student.setUsername(request.getUsername());
        student.setPassword(passwordEncoder.encode(request.getPassword()));
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setEmail(request.getEmail());
        student.setEnabled(false);
        student.setValidationCode(UUID.randomUUID().toString());
        student.setValidationCodeExpiry(Instant.now().plus(Duration.ofHours(1)));
        return student;
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