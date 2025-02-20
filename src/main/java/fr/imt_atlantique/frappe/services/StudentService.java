package fr.imt_atlantique.frappe.services;

import fr.imt_atlantique.frappe.dtos.MeetingRequestDTO;
import fr.imt_atlantique.frappe.dtos.RegistrationRequest;
import fr.imt_atlantique.frappe.dtos.StudentDTO;
import fr.imt_atlantique.frappe.dtos.StudentUpdateRequest;
import fr.imt_atlantique.frappe.entities.Campus;
import fr.imt_atlantique.frappe.entities.CreditTransfer;
import fr.imt_atlantique.frappe.entities.Student;
import fr.imt_atlantique.frappe.exceptions.StudentNotFoundException;
import fr.imt_atlantique.frappe.repositories.CampusRepository;
import fr.imt_atlantique.frappe.repositories.CreditTransferRepository;
import fr.imt_atlantique.frappe.repositories.StudentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;
    private final CampusRepository campusRepository;
    private final CreditTransferRepository creditTransferRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentService(StudentRepository studentRepository, ModelMapper modelMapper, CampusRepository campusRepository, CreditTransferRepository creditTransferRepository, PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.modelMapper = modelMapper;
        this.campusRepository = campusRepository;
        this.creditTransferRepository = creditTransferRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<StudentDTO> getMe(Principal principal) {
        Optional<Student> student = studentRepository.findByUsername(principal.getName());
        if (student.isPresent()) {
            modelMapper.map(student.get(), StudentDTO.class);
            return ResponseEntity.ok(modelMapper.map(student.get(), StudentDTO.class));
        }
        return ResponseEntity.notFound().build();
    }


    @Transactional
    public ResponseEntity<StudentDTO> updateMe(StudentUpdateRequest request, Principal principal) {
        Optional<Student> student = studentRepository.findByUsername(principal.getName());
        if (student.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Student studentToUpdate = student.get();
        if (request.getPassword() != null && isValidPassword(request.getPassword()) ) {
            studentToUpdate.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getFirstName() != null) {
            studentToUpdate.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            studentToUpdate.setLastName(request.getLastName());
        }
        if (request.getPhoneNumber() != null) {
            studentToUpdate.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getCampusId() != null ) {
            Optional<Campus> campus = campusRepository.findById(request.getCampusId());
            if (campus.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            studentToUpdate.setCampus(campus.get());
        }
        if (request.getGender() != null) {
            studentToUpdate.setGender(request.getGender());
        }
        if (request.getNationality() != null) {
            studentToUpdate.setNationality(request.getNationality());
        }
        if (request.getCreditTransferId() != null) {
            Optional<CreditTransfer> creditTransfer = creditTransferRepository.findById(request.getCreditTransferId());
            if (creditTransfer.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            studentToUpdate.setCreditTransfer(creditTransfer.get());
        }
        studentRepository.save(studentToUpdate);
        return ResponseEntity.ok(modelMapper.map(studentToUpdate, StudentDTO.class));
    }

    private boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return Pattern.matches(passwordRegex, password);
    }

    public List<MeetingRequestDTO> getMeetingRequests(Principal principal) {
        Student student = studentRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));
        return student.getMeetingRequests().stream()
                .map(meetingRequest -> modelMapper.map(meetingRequest, MeetingRequestDTO.class))
                .toList();
    }

    public StudentDTO getStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));
        return modelMapper.map(student, StudentDTO.class);
    }
}
