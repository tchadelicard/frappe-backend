package fr.imt_atlantique.frappe.services;

import fr.imt_atlantique.frappe.dtos.MeetingRequestDTO;
import fr.imt_atlantique.frappe.entities.MeetingRequest;
import fr.imt_atlantique.frappe.entities.Student;
import fr.imt_atlantique.frappe.entities.Supervisor;
import fr.imt_atlantique.frappe.repositories.MeetingRequestRepository;
import fr.imt_atlantique.frappe.repositories.StudentRepository;
import fr.imt_atlantique.frappe.repositories.SupervisorRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MeetingRequestService {

    private final MeetingRequestRepository meetingRequestRepository;
    private final StudentRepository studentRepository;
    private final SupervisorRepository supervisorRepository;

    public MeetingRequestService(MeetingRequestRepository meetingRequestRepository, 
                                 StudentRepository studentRepository, 
                                 SupervisorRepository supervisorRepository) {
        this.meetingRequestRepository = meetingRequestRepository;
        this.studentRepository = studentRepository;
        this.supervisorRepository = supervisorRepository;
    }

    public void createMeetingRequest(MeetingRequestDTO meetingRequestDTO) {
        MeetingRequest meetingRequest = new MeetingRequest();
        meetingRequest.setStartDate(meetingRequestDTO.getStartDate());
        meetingRequest.setEndDate(meetingRequestDTO.getEndDate());
        meetingRequest.setTheme(meetingRequestDTO.getTheme());
        meetingRequest.setLocation(meetingRequestDTO.getLocation());
        meetingRequest.setRequestDescription(meetingRequestDTO.getRequestDescription());
        meetingRequest.setStatus(meetingRequestDTO.getStatus());
        
        meetingRequest.setStudent(studentRepository.findById(meetingRequestDTO.getStudentId())
    .orElseThrow(() -> new RuntimeException("Student not found")));
meetingRequest.setSupervisor(supervisorRepository.findById(meetingRequestDTO.getSupervisorId())
    .orElseThrow(() -> new RuntimeException("Supervisor not found")));


   
        meetingRequestRepository.save(meetingRequest);
    }
}
