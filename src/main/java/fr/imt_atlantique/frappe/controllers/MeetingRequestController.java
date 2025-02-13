package fr.imt_atlantique.frappe.controllers;

import fr.imt_atlantique.frappe.dtos.MeetingRequestDTO;
import fr.imt_atlantique.frappe.services.MeetingRequestService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/meeting-requests")
public class MeetingRequestController {

    private final MeetingRequestService meetingRequestService;

    public MeetingRequestController(MeetingRequestService meetingRequestService) {
        this.meetingRequestService = meetingRequestService;
    }

    @Transactional
    @PostMapping
    public ResponseEntity<MeetingRequestDTO> createMeetingRequest(@Valid @RequestBody MeetingRequestDTO request) throws MessagingException, IOException {
        MeetingRequestDTO meetingRequestDTO = meetingRequestService.createMeetingRequest(request);
        return ResponseEntity.ok(meetingRequestDTO);
    }
}