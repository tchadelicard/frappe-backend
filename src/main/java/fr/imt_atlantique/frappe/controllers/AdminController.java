package fr.imt_atlantique.frappe.controllers;

import fr.imt_atlantique.frappe.dtos.RegistrationResponse;
import fr.imt_atlantique.frappe.dtos.SupervisorRequest;
import fr.imt_atlantique.frappe.entities.User;
import fr.imt_atlantique.frappe.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AuthService authService;

    public AdminController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/add-supervisor")
    public ResponseEntity<RegistrationResponse> addSupervisor(@RequestBody SupervisorRequest request) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!currentUser.isSupervisor()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    RegistrationResponse.builder()
                            .success(false)
                            .message("Only supervisors can add other supervisors.")
                            .build()
            );
        }

        RegistrationResponse response = authService.addSupervisor(request);
        if (response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}