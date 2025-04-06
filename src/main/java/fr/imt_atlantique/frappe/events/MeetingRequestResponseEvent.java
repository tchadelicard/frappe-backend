package fr.imt_atlantique.frappe.events;

import java.io.InputStream;

import org.springframework.context.ApplicationEvent;

public class MeetingRequestResponseEvent extends ApplicationEvent {

    public MeetingRequestResponseEvent(InputStream is) {
        super(is);
    }

    public InputStream getInputStream() {
        return (InputStream) getSource();
    }
}
