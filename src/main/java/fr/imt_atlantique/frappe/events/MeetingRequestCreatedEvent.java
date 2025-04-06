package fr.imt_atlantique.frappe.events;

import org.springframework.context.ApplicationEvent;

import fr.imt_atlantique.frappe.entities.MeetingRequest;

public class MeetingRequestCreatedEvent extends ApplicationEvent {

    public MeetingRequestCreatedEvent(MeetingRequest meetingRequest) {
        super(meetingRequest);
    }

    public MeetingRequest getMeetingRequest() {
        return (MeetingRequest) getSource();
    }
}
