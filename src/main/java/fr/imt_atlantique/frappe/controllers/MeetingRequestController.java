package fr.imt_atlantique.frappe.controllers;

import fr.imt_atlantique.frappe.dtos.ActionDTO;
import fr.imt_atlantique.frappe.dtos.CreateMeetingRequestRequest;
import fr.imt_atlantique.frappe.dtos.MeetingRequestDTO;
import fr.imt_atlantique.frappe.entities.Action;
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
    public ResponseEntity<MeetingRequestDTO> createMeetingRequest(@Valid @RequestBody CreateMeetingRequestRequest request) throws MessagingException, IOException {
        MeetingRequestDTO meetingRequestDTO = meetingRequestService.createMeetingRequest(request);
        return ResponseEntity.ok(meetingRequestDTO);
    }

    @GetMapping("/{id}/actions")
    public ResponseEntity<ActionDTO> getAction(@PathVariable Long id) {
        ActionDTO actionDTO = meetingRequestService.getAction(id);
        return ResponseEntity.ok(actionDTO);
    }

    @Transactional
    @PostMapping("/{id}/actions")
    public ResponseEntity<ActionDTO> createAction(@PathVariable Long id, @RequestBody ActionDTO action) {
        ActionDTO actionDTO = meetingRequestService.createAction(id, action);
        return ResponseEntity.ok(actionDTO);
    }
}