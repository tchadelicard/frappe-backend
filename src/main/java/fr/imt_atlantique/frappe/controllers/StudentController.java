package fr.imt_atlantique.frappe.controllers;

import fr.imt_atlantique.frappe.dtos.MeetingRequestDTO;
import fr.imt_atlantique.frappe.dtos.RegistrationRequest;
import fr.imt_atlantique.frappe.dtos.StudentDTO;
import fr.imt_atlantique.frappe.dtos.StudentUpdateRequest;
import fr.imt_atlantique.frappe.services.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudent(@PathVariable Long id) {
        StudentDTO student = studentService.getStudent(id);
        return ResponseEntity.ok(student);
    }

    @GetMapping("/me")
    public ResponseEntity<StudentDTO> getMe(Principal principal) {
        return studentService.getMe(principal);
    }

    @PatchMapping("/me")
    public ResponseEntity<StudentDTO> updateMe(@RequestBody StudentUpdateRequest request, Principal principal) {
        return studentService.updateMe(request, principal);
    }

    @GetMapping("/me/meeting-requests")
    public ResponseEntity<List<MeetingRequestDTO>> getMeetingRequests(Principal principal) {
        List<MeetingRequestDTO> meetingRequestDTO = studentService.getMeetingRequests(principal);
        return ResponseEntity.ok(meetingRequestDTO);
    }
}
