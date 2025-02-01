package fr.imt_atlantique.frappe.controllers;

import fr.imt_atlantique.frappe.dtos.MeetingRequestDTO;
import fr.imt_atlantique.frappe.services.MeetingRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/")
    public ResponseEntity<String> createMeetingRequest(@RequestBody MeetingRequestDTO meetingRequestDTO) {
        meetingRequestService.createMeetingRequest(meetingRequestDTO);
        return ResponseEntity.ok("Meeting request created successfully.");
    }
}