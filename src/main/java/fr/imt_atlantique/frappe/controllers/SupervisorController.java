package fr.imt_atlantique.frappe.controllers;

import fr.imt_atlantique.frappe.dtos.CreateSupervisorRequest;
import fr.imt_atlantique.frappe.dtos.SupervisorDTO;
import fr.imt_atlantique.frappe.services.SupervisorService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> createSupervisor(@RequestBody CreateSupervisorRequest request) {
        SupervisorDTO supervisorDTO = supervisorService.createSupervisor(request);
        return ResponseEntity.ok(supervisorDTO);
    }

    @GetMapping("/me")
    public ResponseEntity<SupervisorDTO> getMe(Principal principal) {
        SupervisorDTO supervisorDTO = supervisorService.getMe(principal);
        return ResponseEntity.ok(supervisorDTO);
    }

}
