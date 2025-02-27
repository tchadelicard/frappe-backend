package fr.imt_atlantique.frappe.services;

import fr.imt_atlantique.frappe.dtos.ActionDTO;
import fr.imt_atlantique.frappe.dtos.AvailabilitySlotDTO;
import fr.imt_atlantique.frappe.dtos.CreateMeetingRequestRequest;
import fr.imt_atlantique.frappe.dtos.MeetingRequestDTO;
import fr.imt_atlantique.frappe.entities.MeetingRequest;
import fr.imt_atlantique.frappe.entities.Student;
import fr.imt_atlantique.frappe.entities.Supervisor;
import fr.imt_atlantique.frappe.repositories.MeetingRequestRepository;
import fr.imt_atlantique.frappe.repositories.StudentRepository;
import fr.imt_atlantique.frappe.repositories.SupervisorRepository;
import jakarta.mail.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Slf4j
public class MeetingRequestService {

    private final MeetingRequestRepository meetingRequestRepository;
    private final StudentRepository studentRepository;
    private final SupervisorRepository supervisorRepository;
    private final AvailabilityService availabilityService;
    private final ModelMapper modelMapper;
    private final CalendarService calendarService;
    private final EmailService emailService;

    @Value("${frappe.mail.host}")
    private String host;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${frappe.mail.from}")
    private String from;

    public MeetingRequestService(MeetingRequestRepository meetingRequestRepository,
            StudentRepository studentRepository,
            SupervisorRepository supervisorRepository,
            AvailabilityService availabilityService, ModelMapper modelMapper,
            CalendarService calendarService, EmailService emailService) {
        this.meetingRequestRepository = meetingRequestRepository;
        this.studentRepository = studentRepository;
        this.supervisorRepository = supervisorRepository;
        this.availabilityService = availabilityService;
        this.modelMapper = modelMapper;
        this.calendarService = calendarService;
        this.emailService = emailService;

    }

    public MeetingRequestDTO createMeetingRequest(CreateMeetingRequestRequest request)
            throws MessagingException, IOException {
        if (!request.getStartDate().toLocalDate().equals(request.getEndDate().toLocalDate())) {
            throw new RuntimeException("Meeting must be on the same day");
        }

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Supervisor supervisor = supervisorRepository.findById(request.getSupervisorId())
                .orElseThrow(() -> new RuntimeException("Supervisor not found"));

        LocalDate date = request.getStartDate().toLocalDate();

        long minutes = ChronoUnit.MINUTES.between(request.getStartDate(), request.getEndDate());

        List<AvailabilitySlotDTO> availableSlots = availabilityService
                .getAvailableSlotsForSupervisor(supervisor.getId(), date, String.valueOf(minutes) + "m");

        boolean isSlotAvailable = availableSlots.stream()
                .anyMatch(slot -> slot.getStart().equals(request.getStartDate().atOffset(ZoneOffset.UTC)) &&
                        slot.getEnd().equals(request.getEndDate().atOffset(ZoneOffset.UTC)));

        if (!isSlotAvailable) {
            throw new RuntimeException("Slot is not available");
        }

        // Create and save the meeting request in the database
        MeetingRequest meetingRequest = new MeetingRequest();
        meetingRequest.setStartDate(request.getStartDate());
        meetingRequest.setEndDate(request.getEndDate());
        meetingRequest.setTheme(request.getTheme());
        meetingRequest.setLocation(request.getLocation());
        meetingRequest.setRequestDescription(request.getRequestDescription());
        meetingRequest.setStatus("PENDING");
        meetingRequest.setStudent(student);
        meetingRequest.setSupervisor(supervisor);

        meetingRequestRepository.save(meetingRequest);

        // Generate ICS file for the meeting
        String ics = calendarService.createMeetingRequestICS(meetingRequest);

        // Send meeting request email with ICS attachment
        emailService.sendMeetingInvitation(meetingRequest, ics);

        return modelMapper.map(meetingRequest, MeetingRequestDTO.class);
    }

    public ActionDTO getAction(Long id) {
        MeetingRequest meetingRequest = meetingRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Meeting request not found"));

        fr.imt_atlantique.frappe.entities.Action action = meetingRequest.getAction();
        if (action == null) {
            throw new RuntimeException("Action not found");
        }

        return modelMapper.map(action, ActionDTO.class);
    }

    public ActionDTO createAction(Long id, ActionDTO actionDTO) {
        MeetingRequest meetingRequest = meetingRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Meeting request not found"));

        fr.imt_atlantique.frappe.entities.Action action = new fr.imt_atlantique.frappe.entities.Action();
        action.setNotes(actionDTO.getNotes());
        action.setActionPlan(actionDTO.getActionPlan());

        meetingRequest.setAction(action);
        meetingRequestRepository.save(meetingRequest);

        return modelMapper.map(action, ActionDTO.class);
    }
}
