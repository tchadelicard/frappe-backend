package fr.imt_atlantique.frappe.controllers;

import fr.imt_atlantique.frappe.dtos.MeetingRequestDTO;
import fr.imt_atlantique.frappe.dtos.StudentDTO;
import fr.imt_atlantique.frappe.dtos.StudentUpdateRequest;
import fr.imt_atlantique.frappe.services.MeetingRequestService;
import fr.imt_atlantique.frappe.services.StudentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;
    private final MeetingRequestService meetingRequestService;

    public StudentController(StudentService studentService, MeetingRequestService meetingRequestService) {
        this.studentService = studentService;
        this.meetingRequestService = meetingRequestService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudent(@Valid @PathVariable @Min(1) Long id) {
        StudentDTO student = studentService.toDTO(studentService.getStudentById(id));
        return ResponseEntity.ok(student);
    }

    @GetMapping("/me")
    public ResponseEntity<StudentDTO> getMe(Principal principal) {
        StudentDTO student = studentService.toDTO(studentService.getMe(principal));
        return ResponseEntity.ok(student);
    }

    @PatchMapping("/me")
    public ResponseEntity<StudentDTO> updateMe(@Valid @RequestBody StudentUpdateRequest request, Principal principal) {
        StudentDTO student = studentService.toDTO(studentService.updateMe(request, principal));
        return ResponseEntity.ok(student);
    }

    @GetMapping("/me/meeting-requests")
    public ResponseEntity<List<MeetingRequestDTO>> getMeetingRequests(Principal principal) {
        List<MeetingRequestDTO> meetingRequestDTO = meetingRequestService
                .toDTOs(studentService.getMeetingRequests(principal));
        return ResponseEntity.ok(meetingRequestDTO);
    }
}
