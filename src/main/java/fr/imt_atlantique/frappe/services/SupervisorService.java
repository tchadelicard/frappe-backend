package fr.imt_atlantique.frappe.services;

import fr.imt_atlantique.frappe.dtos.*;
import fr.imt_atlantique.frappe.entities.Campus;
import fr.imt_atlantique.frappe.entities.Supervisor;
import fr.imt_atlantique.frappe.exceptions.SupervisorNotFoundException;
import fr.imt_atlantique.frappe.repositories.CampusRepository;
import fr.imt_atlantique.frappe.repositories.SupervisorRepository;
import fr.imt_atlantique.frappe.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class SupervisorService {
    private final UserRepository userRepository;
    private final SupervisorRepository supervisorRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final EncryptionService encryptionService;
    private final CampusRepository campusRepository;

    public SupervisorService(UserRepository userRepository, SupervisorRepository supervisorRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, EncryptionService encryptionService, CampusRepository campusRepository) {
        this.userRepository = userRepository;
        this.supervisorRepository = supervisorRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.encryptionService = encryptionService;
        this.campusRepository = campusRepository;
    }

    public List<SupervisorDTO> getSupervisors() {
          return supervisorRepository
                  .findAll()
                  .stream()
                  .map(supervisor -> modelMapper.map(supervisor, SupervisorDTO.class))
                  .toList();
    }

    public SupervisorDTO createSupervisor(CreateSupervisorRequest request) {
        // Encrypt caldavPassword using EncryptionService
        EncryptionResult encryptionResult;
        try {
            encryptionResult = encryptionService.encryptAndPrepareData(request.getCaldavPassword());
        } catch (Exception e) {
            throw new RuntimeException("Failed to create supervisor");
        }

        // Validate request
        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            throw new RuntimeException("Email is already taken.");
        });

        userRepository.findByUsername(request.getUsername()).ifPresent(user -> {
            throw new RuntimeException("Username is already taken.");
        });

        Campus campus = campusRepository.findById(request.getCampusId()).orElseThrow(() -> new RuntimeException("Campus does not exist."));


        // Create supervisor
        Supervisor supervisor = new Supervisor();
        supervisor.setUsername(request.getUsername());
        supervisor.setEmail(request.getEmail());
        supervisor.setPassword(passwordEncoder.encode(request.getPassword()));
        supervisor.setFirstName(request.getFirstName());
        supervisor.setLastName(request.getLastName());
        supervisor.setCampus(campus);
        supervisor.setMeetingUrl(request.getMeetingUrl());
        supervisor.setCaldavUsername(request.getCaldavUsername());
        supervisor.setCaldavPassword(encryptionResult.getEncryptedData()); // Store encrypted password
        supervisor.setCaldavPasswordSalt(encryptionResult.getSalt());      // Store salt
        supervisor.setCaldavPasswordIv(encryptionResult.getIv());          // Store IV
        supervisor.setEnabled(true);

        supervisorRepository.save(supervisor);

        return modelMapper.map(supervisor, SupervisorDTO.class);
    }

    public SupervisorDTO getMe(Principal principal) {
        Supervisor supervisor = supervisorRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new SupervisorNotFoundException("Supervisor not found"));
        return modelMapper.map(supervisor, SupervisorDTO.class);
    }

    public SupervisorDTO updateMe(Principal principal, UpdateSupervisorRequest request) {
        Supervisor supervisor = supervisorRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new SupervisorNotFoundException("Supervisor not found"));

        // Validate username uniqueness if provided and different from the current one
        if (request.getUsername() != null && !request.getUsername().equals(supervisor.getUsername())) {
            if (userRepository.findByUsername(request.getUsername()).isPresent()) {
                throw new RuntimeException("Username is already taken.");
            }
            supervisor.setUsername(request.getUsername());
        }

        // Validate email uniqueness if provided and different from the current one
        if (request.getEmail() != null && !request.getEmail().equals(supervisor.getEmail())) {
            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                throw new RuntimeException("Email is already taken.");
            }
            supervisor.setEmail(request.getEmail());
        }

        // Update password if provided (regex in DTO ensures validity)
        if (request.getPassword() != null) {
            supervisor.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        // Update first name if provided
        if (request.getFirstName() != null) {
            supervisor.setFirstName(request.getFirstName());
        }

        // Update last name if provided
        if (request.getLastName() != null) {
            supervisor.setLastName(request.getLastName());
        }

        // Update campus if provided
        if (request.getCampusId() != null) {
            Campus campus = campusRepository.findById(request.getCampusId())
                    .orElseThrow(() -> new RuntimeException("Campus does not exist."));
            supervisor.setCampus(campus);
        }

        // Update optional fields if provided
        if (request.getMeetingUrl() != null) {
            supervisor.setMeetingUrl(request.getMeetingUrl());
        }

        if (request.getCaldavUsername() != null) {
            supervisor.setCaldavUsername(request.getCaldavUsername());
        }

        if (request.getCaldavPassword() != null) {
            supervisor.setCaldavPassword(request.getCaldavPassword());
        }

        // Save updated supervisor
        supervisorRepository.save(supervisor);

        return modelMapper.map(supervisor, SupervisorDTO.class);
    }

    public List<MeetingRequestDTO> getMeetingRequests(Long id) {
        Supervisor supervisor = supervisorRepository.findById(id)
                .orElseThrow(() -> new SupervisorNotFoundException("Supervisor not found"));
        return supervisor.getMeetingRequests()
                .stream()
                .map(meetingRequest -> modelMapper.map(meetingRequest, MeetingRequestDTO.class))
                .toList();
    }

    public List<MeetingRequestDTO> getMeetingRequests(Principal principal) {
        Supervisor supervisor = supervisorRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new SupervisorNotFoundException("Supervisor not found"));
        return supervisor.getMeetingRequests()
                .stream()
                .map(meetingRequest -> modelMapper.map(meetingRequest, MeetingRequestDTO.class))
                .toList();
    }

    public SupervisorDTO getSupervisor(Long id) {
        Supervisor supervisor = supervisorRepository.findById(id)
                .orElseThrow(() -> new SupervisorNotFoundException("Supervisor not found"));
        return modelMapper.map(supervisor, SupervisorDTO.class);
    }
}
