package fr.imt_atlantique.frappe.services;

import fr.imt_atlantique.frappe.dtos.CreateSupervisorRequest;
import fr.imt_atlantique.frappe.dtos.EncryptionResult;
import fr.imt_atlantique.frappe.dtos.RegistrationRequest;
import fr.imt_atlantique.frappe.dtos.SupervisorDTO;
import fr.imt_atlantique.frappe.entities.Supervisor;
import fr.imt_atlantique.frappe.repositories.SupervisorRepository;
import fr.imt_atlantique.frappe.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class SupervisorService {
    private final UserRepository userRepository;
    private final SupervisorRepository supervisorRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final EncryptionService encryptionService;

    public SupervisorService(UserRepository userRepository, SupervisorRepository supervisorRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, EncryptionService encryptionService) {
        this.userRepository = userRepository;
        this.supervisorRepository = supervisorRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.encryptionService = encryptionService;
    }

    public ResponseEntity<List<SupervisorDTO>> getSupervisors() {
        return ResponseEntity.ok(
          supervisorRepository
                  .findAll()
                  .stream()
                  .map(supervisor -> modelMapper.map(supervisor, SupervisorDTO.class))
                  .toList()
        );
    }

    public ResponseEntity<?> createSupervisor(CreateSupervisorRequest request) {
        String validationError = validateCreateSupervisorRequest(request);
        if (validationError != null) {
            return ResponseEntity.badRequest().body(validationError);
        }

        try {
            // Encrypt caldavPassword using EncryptionService
            EncryptionResult encryptionResult = encryptionService.encryptAndPrepareData(request.getCaldavPassword());

            // Create supervisor
            Supervisor supervisor = new Supervisor();
            supervisor.setUsername(request.getUsername());
            supervisor.setEmail(request.getEmail());
            supervisor.setPassword(passwordEncoder.encode(request.getPassword()));
            supervisor.setFirstName(request.getFirstName());
            supervisor.setLastName(request.getLastName());
            supervisor.setMeetingUrl(request.getMeetingUrl());
            supervisor.setCaldavUsername(request.getCaldavUsername());
            supervisor.setCaldavPassword(encryptionResult.getEncryptedData()); // Store encrypted password
            supervisor.setCaldavPasswordSalt(encryptionResult.getSalt());      // Store salt
            supervisor.setCaldavPasswordIv(encryptionResult.getIv());          // Store IV
            supervisor.setEnabled(true);

            supervisorRepository.save(supervisor);

            return ResponseEntity.ok(modelMapper.map(supervisor, SupervisorDTO.class));
        } catch (Exception e) {
            // Log the error and return a generic error message
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while creating supervisor.");
        }
    }
    private String validateCreateSupervisorRequest(CreateSupervisorRequest request) {
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
