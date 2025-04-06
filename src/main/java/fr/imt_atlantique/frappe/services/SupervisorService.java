package fr.imt_atlantique.frappe.services;

import java.security.Principal;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import fr.imt_atlantique.frappe.dtos.CreateSupervisorRequest;
import fr.imt_atlantique.frappe.dtos.EncryptionResult;
import fr.imt_atlantique.frappe.dtos.MinimalSupervisorDTO;
import fr.imt_atlantique.frappe.dtos.MinimalUserDTO;
import fr.imt_atlantique.frappe.dtos.SupervisorDTO;
import fr.imt_atlantique.frappe.dtos.UpdateSupervisorRequest;
import fr.imt_atlantique.frappe.entities.Campus;
import fr.imt_atlantique.frappe.entities.MeetingRequest;
import fr.imt_atlantique.frappe.entities.Supervisor;
import fr.imt_atlantique.frappe.exceptions.SupervisorNotFoundException;
import fr.imt_atlantique.frappe.repositories.SupervisorRepository;

@Service
public class SupervisorService {
    private final UserService userService;
    private final SupervisorRepository supervisorRepository;
    private final ModelMapper modelMapper;
    private final EncryptionService encryptionService;
    private final CampusService campusService;

    public SupervisorService(UserService userService, SupervisorRepository supervisorRepository,
            ModelMapper modelMapper, EncryptionService encryptionService,
            CampusService campusService) {
        this.userService = userService;
        this.supervisorRepository = supervisorRepository;
        this.modelMapper = modelMapper;
        this.encryptionService = encryptionService;
        this.campusService = campusService;
    }

    public MinimalUserDTO toDTO(Supervisor supervisor) {
        List<String> roles = userService.getUserRoles();

        if (roles.contains("ROLE_STUDENT"))
            return modelMapper.map(supervisor, MinimalSupervisorDTO.class);

        return modelMapper.map(supervisor, SupervisorDTO.class);
    }

    public List<MinimalUserDTO> toDTOs(List<Supervisor> supervisors) {
        return supervisors.stream()
                .map(supervisor -> toDTO(supervisor))
                .toList();
    }

    public List<Supervisor> getSupervisors() {
        return supervisorRepository
                .findAll()
                .stream()
                .toList();
    }

    public Supervisor createSupervisor(CreateSupervisorRequest request) {
        userService.ensureUserEmailDoesNotExist(request.getEmail());
        userService.ensureUserUsernameDoesNotExist(request.getUsername());

        Campus campus = campusService.getCampusById(request.getCampusId());

        // Create supervisor
        Supervisor supervisor = new Supervisor();
        supervisor.setUsername(request.getUsername());
        supervisor.setEmail(request.getEmail());
        supervisor.setPassword(userService.hashPassword(request.getPassword()));
        supervisor.setFirstName(request.getFirstName());
        supervisor.setLastName(request.getLastName());
        supervisor.setCampus(campus);
        supervisor.setEnabled(true);

        supervisorRepository.save(supervisor);

        return supervisor;
    }

    public Supervisor getMe(Principal principal) {
        Supervisor supervisor = supervisorRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new SupervisorNotFoundException("Supervisor not found"));
        return supervisor;
    }

    public Supervisor updateMe(Principal principal, UpdateSupervisorRequest request) {
        Supervisor supervisor = supervisorRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new SupervisorNotFoundException("Supervisor not found"));

        // Validate username uniqueness if provided and different from the current one
        if (request.getUsername() != null && !request.getUsername().equals(supervisor.getUsername())) {
            userService.ensureUserEmailDoesNotExist(request.getUsername());
            supervisor.setUsername(request.getUsername());
        }

        // Validate email uniqueness if provided and different from the current one
        if (request.getEmail() != null && !request.getEmail().equals(supervisor.getEmail())) {
            userService.ensureUserEmailDoesNotExist(request.getEmail());
            supervisor.setEmail(request.getEmail());
        }

        // Update password if provided (regex in DTO ensures validity)
        if (request.getPassword() != null) {
            supervisor.setPassword(userService.hashPassword(request.getPassword()));
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
            Campus campus = campusService.getCampusById(request.getCampusId());
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
            try {
                EncryptionResult encryptionResult = encryptionService
                        .encryptAndPrepareData(request.getCaldavPassword());
                supervisor.setCaldavPassword(encryptionResult.getEncryptedData());
                supervisor.setCaldavPasswordIv(encryptionResult.getIv());
                supervisor.setCaldavPasswordSalt(encryptionResult.getSalt());
            } catch (Exception e) {
                throw new RuntimeException("Error encrypting password");
            }
        }

        // Save updated supervisor
        supervisorRepository.save(supervisor);

        return supervisor;
    }

    public List<MeetingRequest> getMeetingRequests(Long id) {
        Supervisor supervisor = supervisorRepository.findById(id)
                .orElseThrow(() -> new SupervisorNotFoundException("Supervisor not found"));
        return supervisor.getMeetingRequests()
                .stream()
                .toList();
    }

    public List<MeetingRequest> getMeetingRequests(Principal principal) {
        Supervisor supervisor = supervisorRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new SupervisorNotFoundException("Supervisor not found"));
        return supervisor.getMeetingRequests()
                .stream()
                .toList();
    }

    public Supervisor getSupervisorById(Long id) {
        return supervisorRepository.findById(id)
                .orElseThrow(() -> new SupervisorNotFoundException("Supervisor not found"));
    }
}
