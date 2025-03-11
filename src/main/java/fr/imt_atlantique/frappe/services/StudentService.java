package fr.imt_atlantique.frappe.services;

import java.security.Principal;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.imt_atlantique.frappe.dtos.StudentDTO;
import fr.imt_atlantique.frappe.dtos.StudentUpdateRequest;
import fr.imt_atlantique.frappe.entities.Campus;
import fr.imt_atlantique.frappe.entities.CreditTransfer;
import fr.imt_atlantique.frappe.entities.MeetingRequest;
import fr.imt_atlantique.frappe.entities.Student;
import fr.imt_atlantique.frappe.exceptions.StudentNotFoundException;
import fr.imt_atlantique.frappe.repositories.StudentRepository;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;
    private final CampusService campusService;
    private final CreditTransferService creditTransferService;
    private final PasswordEncoder passwordEncoder;

    public StudentService(StudentRepository studentRepository, ModelMapper modelMapper,
            CampusService campusService, CreditTransferService creditTransferService,
            PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.modelMapper = modelMapper;
        this.campusService = campusService;
        this.creditTransferService = creditTransferService;
        this.passwordEncoder = passwordEncoder;
    }

    public StudentDTO toDTO(Student student) {
        return modelMapper.map(student, StudentDTO.class);
    }

    public List<StudentDTO> toDTOs(List<Student> students) {
        return students.stream()
                .map(student -> modelMapper.map(student, StudentDTO.class))
                .toList();
    }

    public Student getMe(Principal principal) {
        return studentRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));
    }

    @Transactional
    public Student updateMe(StudentUpdateRequest request, Principal principal) {
        Student student = studentRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));
        if (request.getPassword() != null) {
            student.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getFirstName() != null) {
            student.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            student.setLastName(request.getLastName());
        }
        if (request.getPhoneNumber() != null) {
            student.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getCampusId() != null) {
            Campus campus = campusService.getCampusById(request.getCampusId());
            student.setCampus(campus);
        }
        if (request.getGender() != null) {
            student.setGender(request.getGender());
        }
        if (request.getNationality() != null) {
            student.setNationality(request.getNationality());
        }
        if (request.getCreditTransferId() != null) {
            CreditTransfer creditTransfer = creditTransferService.getCreditTransferById(request.getCreditTransferId());
            student.setCreditTransfer(creditTransfer);
        }
        studentRepository.save(student);
        return student;
    }

    public List<MeetingRequest> getMeetingRequests(Principal principal) {
        Student student = studentRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));
        return student.getMeetingRequests().stream()
                .toList();
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));
    }
}
