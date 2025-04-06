package fr.imt_atlantique.frappe.controllers;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.imt_atlantique.frappe.dtos.ActionDTO;
import fr.imt_atlantique.frappe.dtos.CreateMeetingRequestRequest;
import fr.imt_atlantique.frappe.dtos.MeetingRequestDTO;
import fr.imt_atlantique.frappe.services.MeetingRequestService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/meeting-requests")
public class MeetingRequestController {

    private final MeetingRequestService meetingRequestService;

    public MeetingRequestController(MeetingRequestService meetingRequestService) {
        this.meetingRequestService = meetingRequestService;
    }

    @PostMapping
    public ResponseEntity<MeetingRequestDTO> createMeetingRequest(
            @Valid @RequestBody CreateMeetingRequestRequest request) throws MessagingException, IOException {
        MeetingRequestDTO meetingRequestDTO = meetingRequestService
                .toDTO(meetingRequestService.createMeetingRequest(request));
        return ResponseEntity.ok(meetingRequestDTO);
    }

    @GetMapping("/{id}/actions")
    public ResponseEntity<ActionDTO> getAction(@PathVariable Long id) {
        ActionDTO actionDTO = meetingRequestService.toActionDTO(meetingRequestService.getAction(id));
        return ResponseEntity.ok(actionDTO);
    }

    @PostMapping("/{id}/actions")
    public ResponseEntity<ActionDTO> createAction(@PathVariable Long id, @RequestBody ActionDTO action) {
        ActionDTO actionDTO = meetingRequestService.toActionDTO(meetingRequestService.createAction(id, action));
        return ResponseEntity.ok(actionDTO);
    }
}