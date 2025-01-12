package fr.imt_atlantique.frappe.controllers;

import fr.imt_atlantique.frappe.dtos.CreateSupervisorRequest;
import fr.imt_atlantique.frappe.dtos.SupervisorDTO;
import fr.imt_atlantique.frappe.services.SupervisorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/supervisors")
public class SupervisorController {

    private final SupervisorService supervisorService;

    public SupervisorController(SupervisorService supervisorService) {
        this.supervisorService = supervisorService;
    }

    @GetMapping("/")
    public ResponseEntity<List<SupervisorDTO>> getSupervisors() {
        return supervisorService.getSupervisors();
    }

    @PostMapping("/")
    public ResponseEntity<?> createSupervisor(@RequestBody CreateSupervisorRequest request) {
        return supervisorService.createSupervisor(request);
    }

}
