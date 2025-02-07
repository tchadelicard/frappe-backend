package fr.imt_atlantique.frappe.controllers;

import fr.imt_atlantique.frappe.dtos.MeetingRequestDTO;
import fr.imt_atlantique.frappe.services.MeetingRequestService;
import jakarta.mail.MessagingException;
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
    @PostMapping("/hello")
    public ResponseEntity<String> hh(@RequestBody String  meetingRequestDTO) {

        return ResponseEntity.ok("Meeting request created successfully.");
    }

    @Transactional
    @PostMapping
    public ResponseEntity<String> createMeetingRequest(@RequestBody MeetingRequestDTO meetingRequestDTO) throws MessagingException, IOException {
        meetingRequestService.createMeetingRequest(meetingRequestDTO);
        return ResponseEntity.ok("Meeting request created successfully.");
    }
}