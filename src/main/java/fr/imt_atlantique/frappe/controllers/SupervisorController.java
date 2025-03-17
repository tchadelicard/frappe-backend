package fr.imt_atlantique.frappe.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.imt_atlantique.frappe.dtos.CreateSupervisorRequest;
import fr.imt_atlantique.frappe.dtos.MeetingRequestDTO;
import fr.imt_atlantique.frappe.dtos.MinimalUserDTO;
import fr.imt_atlantique.frappe.dtos.UpdateSupervisorRequest;
import fr.imt_atlantique.frappe.services.MeetingRequestService;
import fr.imt_atlantique.frappe.services.SupervisorService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/supervisors")
public class SupervisorController {

    private final SupervisorService supervisorService;
    private final MeetingRequestService meetingRequestService;

    public SupervisorController(SupervisorService supervisorService, MeetingRequestService meetingRequestService) {
        this.supervisorService = supervisorService;
        this.meetingRequestService = meetingRequestService;
    }

    @GetMapping
    public ResponseEntity<List<MinimalUserDTO>> getSupervisors() {
        List<MinimalUserDTO> supervisors = supervisorService.toDTOs(supervisorService.getSupervisors());
        return ResponseEntity.ok(supervisors);
    }

    @PostMapping
    public ResponseEntity<MinimalUserDTO> createSupervisor(@Valid @RequestBody CreateSupervisorRequest request) {
        MinimalUserDTO supervisor = supervisorService.toDTO(supervisorService.createSupervisor(request));
        return ResponseEntity.ok(supervisor);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MinimalUserDTO> getSupervisor(@PathVariable Long id) {
        MinimalUserDTO supervisor = supervisorService.toDTO(supervisorService.getSupervisorById(id));
        return ResponseEntity.ok(supervisor);
    }

    @GetMapping("/{id}/meeting-requests")
    public ResponseEntity<List<MeetingRequestDTO>> getMeetingRequests(@PathVariable Long id) {
        List<MeetingRequestDTO> meetingRequests = meetingRequestService
                .toDTOs(supervisorService.getMeetingRequests(id));
        return ResponseEntity.ok(meetingRequests);
    }

    @GetMapping("/me")
    public ResponseEntity<MinimalUserDTO> getMe(Principal principal) {
        MinimalUserDTO supervisor = supervisorService.toDTO(supervisorService.getMe(principal));
        return ResponseEntity.ok(supervisor);
    }

    @PatchMapping("/me")
    public ResponseEntity<MinimalUserDTO> updateMe(Principal principal,
            @Valid @RequestBody UpdateSupervisorRequest request) {
        MinimalUserDTO supervisor = supervisorService.toDTO(supervisorService.updateMe(principal, request));
        return ResponseEntity.ok(supervisor);
    }

    @GetMapping("/me/meeting-requests")
    public ResponseEntity<List<MeetingRequestDTO>> getMeetingRequests(Principal principal) {
        List<MeetingRequestDTO> meetingRequests = meetingRequestService
                .toDTOs(supervisorService.getMeetingRequests(principal));
        return ResponseEntity.ok(meetingRequests);
    }
}
