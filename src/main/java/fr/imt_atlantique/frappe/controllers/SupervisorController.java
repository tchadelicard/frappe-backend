package fr.imt_atlantique.frappe.controllers;

import fr.imt_atlantique.frappe.dtos.CreateSupervisorRequest;
import fr.imt_atlantique.frappe.dtos.MeetingRequestDTO;
import fr.imt_atlantique.frappe.dtos.SupervisorDTO;
import fr.imt_atlantique.frappe.services.SupervisorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/supervisors")
public class SupervisorController {

    private final SupervisorService supervisorService;

    public SupervisorController(SupervisorService supervisorService) {
        this.supervisorService = supervisorService;
    }

    @GetMapping
    public ResponseEntity<List<SupervisorDTO>> getSupervisors() {
        return ResponseEntity.ok(supervisorService.getSupervisors());
    }

    @PostMapping
    public ResponseEntity<SupervisorDTO> createSupervisor(@Valid @RequestBody CreateSupervisorRequest request) {
        SupervisorDTO supervisorDTO = supervisorService.createSupervisor(request);
        return ResponseEntity.ok(supervisorDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupervisorDTO> getSupervisor(@PathVariable Long id) {
        SupervisorDTO supervisorDTO = supervisorService.getSupervisor(id);
        return ResponseEntity.ok(supervisorDTO);
    }

    @GetMapping("/{id}/meeting-requests")
    public ResponseEntity<List<MeetingRequestDTO>> getMeetingRequests(@PathVariable Long id) {
        List<MeetingRequestDTO> meetingRequestDTOs = supervisorService.getMeetingRequests(id);
        return ResponseEntity.ok(meetingRequestDTOs);
    }

    @GetMapping("/me")
    public ResponseEntity<SupervisorDTO> getMe(Principal principal) {
        SupervisorDTO supervisorDTO = supervisorService.getMe(principal);
        return ResponseEntity.ok(supervisorDTO);
    }

    @GetMapping("/me/meeting-requests")
    public ResponseEntity<List<MeetingRequestDTO>> getMeetingRequests(Principal principal) {
        List<MeetingRequestDTO> meetingRequestDTOs = supervisorService.getMeetingRequests(principal);
        return ResponseEntity.ok(meetingRequestDTOs);
    }
}
