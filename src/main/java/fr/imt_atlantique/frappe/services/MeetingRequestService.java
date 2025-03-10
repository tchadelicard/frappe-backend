package fr.imt_atlantique.frappe.services;

import fr.imt_atlantique.frappe.dtos.ActionDTO;
import fr.imt_atlantique.frappe.dtos.AvailabilitySlotDTO;
import fr.imt_atlantique.frappe.dtos.CreateMeetingRequestRequest;
import fr.imt_atlantique.frappe.dtos.MeetingRequestDTO;
import fr.imt_atlantique.frappe.entities.Action;
import fr.imt_atlantique.frappe.entities.MeetingRequest;
import fr.imt_atlantique.frappe.entities.Student;
import fr.imt_atlantique.frappe.entities.Supervisor;
import fr.imt_atlantique.frappe.events.MeetingRequestCreatedEvent;
import fr.imt_atlantique.frappe.repositories.MeetingRequestRepository;
import jakarta.mail.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
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
    private final StudentService studentService;
    private final SupervisorService supervisorService;
    private final AvailabilityService availabilityService;
    private final ModelMapper modelMapper;
    private final ApplicationEventPublisher eventPublisher;

    public MeetingRequestService(MeetingRequestRepository meetingRequestRepository,
            StudentService studentService,
            SupervisorService supervisorService,
            AvailabilityService availabilityService,
            ModelMapper modelMapper,
            ApplicationEventPublisher eventPublisher) {
        this.meetingRequestRepository = meetingRequestRepository;
        this.studentService = studentService;
        this.supervisorService = supervisorService;
        this.availabilityService = availabilityService;
        this.modelMapper = modelMapper;
        this.eventPublisher = eventPublisher;
    }

    public MeetingRequestDTO toDTO(MeetingRequest meetingRequest) {
        return modelMapper.map(meetingRequest, MeetingRequestDTO.class);
    }

    public List<MeetingRequestDTO> toDTOs(List<MeetingRequest> meetingRequests) {
        return meetingRequests.stream()
                .map(meetingRequest -> modelMapper.map(meetingRequest, MeetingRequestDTO.class))
                .toList();
    }

    public MeetingRequestDTO createMeetingRequest(CreateMeetingRequestRequest request)
            throws MessagingException, IOException {
        validateMeetingRequest(request);

        Student student = studentService.getStudentById(request.getStudentId());
        Supervisor supervisor = supervisorService.getSupervisorById(request.getSupervisorId());

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

        MeetingRequest meetingRequest = saveMeetingRequest(request, student, supervisor);

        eventPublisher.publishEvent(new MeetingRequestCreatedEvent(meetingRequest));

        return modelMapper.map(meetingRequest, MeetingRequestDTO.class);
    }

    private void validateMeetingRequest(CreateMeetingRequestRequest request) {
        if (!request.getStartDate().toLocalDate().equals(request.getEndDate().toLocalDate())) {
            throw new RuntimeException("Meeting must be on the same day");
        }
    }

    private MeetingRequest saveMeetingRequest(CreateMeetingRequestRequest request, Student student,
            Supervisor supervisor) {
        MeetingRequest meetingRequest = new MeetingRequest();
        meetingRequest.setStartDate(request.getStartDate());
        meetingRequest.setEndDate(request.getEndDate());
        meetingRequest.setTheme(request.getTheme());
        meetingRequest.setLocation(request.getLocation());
        meetingRequest.setRequestDescription(request.getRequestDescription());
        meetingRequest.setStatus("PENDING");
        meetingRequest.setStudent(student);
        meetingRequest.setSupervisor(supervisor);

        return meetingRequestRepository.save(meetingRequest);
    }

    public void updateMeetingRequestStatus(String email, Long meetingRequestId, String status) {
        MeetingRequest meetingRequest = meetingRequestRepository.findById(meetingRequestId)
                .orElseThrow(() -> new RuntimeException("Meeting request not found"));
        meetingRequest.setStatus(status);
        meetingRequestRepository.save(meetingRequest);
        log.info("✅ Meeting request updated for: {}", email);
    }

    public ActionDTO getAction(Long id) {
        MeetingRequest meetingRequest = meetingRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Meeting request not found"));

        Action action = meetingRequest.getAction();
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
