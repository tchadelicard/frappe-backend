package fr.imt_atlantique.frappe.controllers;

import fr.imt_atlantique.frappe.dtos.RegistrationRequest;
import fr.imt_atlantique.frappe.dtos.StudentDTO;
import fr.imt_atlantique.frappe.dtos.StudentUpdateRequest;
import fr.imt_atlantique.frappe.services.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/me")
    public ResponseEntity<StudentDTO> getMe(Principal principal) {
        return studentService.getMe(principal);
    }

    @PatchMapping("/me")
    public ResponseEntity<StudentDTO> updateMe(@RequestBody StudentUpdateRequest request, Principal principal) {
        return studentService.updateMe(request, principal);
    }
}
