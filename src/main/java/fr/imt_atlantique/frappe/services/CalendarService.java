package fr.imt_atlantique.frappe.services;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import fr.imt_atlantique.frappe.entities.MeetingRequest;
import fr.imt_atlantique.frappe.events.MeetingRequestCreatedEvent;
import fr.imt_atlantique.frappe.events.MeetingRequestResponseEvent;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.component.VAlarm;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.parameter.Cn;
import net.fortuna.ical4j.model.parameter.PartStat;
import net.fortuna.ical4j.model.parameter.Role;
import net.fortuna.ical4j.model.parameter.Rsvp;
import net.fortuna.ical4j.model.property.Action;
import net.fortuna.ical4j.model.property.Attendee;
import net.fortuna.ical4j.model.property.Clazz;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.LastModified;
import net.fortuna.ical4j.model.property.Method;
import net.fortuna.ical4j.model.property.Organizer;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Sequence;
import net.fortuna.ical4j.model.property.Status;
import net.fortuna.ical4j.model.property.Transp;
import net.fortuna.ical4j.model.property.Trigger;

@Service
@Slf4j
public class CalendarService {

    private final EmailService emailService;
    private final MeetingRequestService meetingRequestService;

    public CalendarService(EmailService emailService, MeetingRequestService meetingRequestService) {
        this.emailService = emailService;
        this.meetingRequestService = meetingRequestService;
    }

    @EventListener
    public void handleMeetingRequestCreatedEvent(MeetingRequestCreatedEvent event)
            throws IOException, MessagingException {
        MeetingRequest meetingRequest = event.getMeetingRequest();
        String ics = createMeetingRequestICS(meetingRequest);
        emailService.sendMeetingInvitation(meetingRequest, ics);
    }

    @EventListener
    public void handleMeetingRequestResponseEvent(MeetingRequestResponseEvent event) {
        processCalendar(event.getInputStream());
    }

    public String createMeetingRequestICS(MeetingRequest meetingRequest) {
        VTimeZone tz = generateVTimeZone();
        VEvent vevent = createVEvent(meetingRequest);

        Calendar calendar = createCalendar(tz, vevent);

        return calendar.toString();
    }

    private VTimeZone generateVTimeZone() {
        TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
        return registry.getTimeZone("Europe/Paris").getVTimeZone();
    }

    private ZonedDateTime convertLocalDateTimeToZonedDateTime(LocalDateTime date) {
        return date.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("Europe/Paris"));
    }

    private VEvent createVEvent(MeetingRequest meetingRequest) {
        ZonedDateTime startDate = convertLocalDateTimeToZonedDateTime(meetingRequest.getStartDate());
        ZonedDateTime endDate = convertLocalDateTimeToZonedDateTime(meetingRequest.getEndDate());

        VEvent vevent = (VEvent) new VEvent(startDate, endDate, meetingRequest.getTheme())
                .withProperty(new Description(meetingRequest.getRequestDescription()))
                .withProperty(new Attendee("mailto:" + meetingRequest.getStudent().getEmail())
                        .withParameter(new Cn(meetingRequest.getStudent().getFirstName() + " "
                                + meetingRequest.getStudent().getLastName()))
                        .withParameter(Role.REQ_PARTICIPANT)
                        .withParameter(new PartStat("NEEDS-ACTION"))
                        .withParameter(new Rsvp(true))
                        .getFluentTarget())
                .withProperty(new Attendee("mailto:" + meetingRequest.getSupervisor().getEmail())
                        .withParameter(new Cn(meetingRequest.getSupervisor().getFirstName() + " "
                                + meetingRequest.getSupervisor().getLastName()))
                        .withParameter(Role.REQ_PARTICIPANT)
                        .withParameter(new PartStat("NEEDS-ACTION"))
                        .withParameter(new Rsvp(true))
                        .getFluentTarget())
                .withProperty(new Organizer("mailto:tchadelicard@icloud.com")
                        .withParameter(new Cn("Tchadel Icard"))
                        .getFluentTarget())
                .withProperty(new Status("CONFIRMED"))
                .withProperty(new Clazz("PUBLIC"))
                .withProperty(new Transp("OPAQUE"))
                .withProperty(new Sequence(0))
                .withProperty(new LastModified(ZonedDateTime.now().toInstant()))
                .getFluentTarget();

        VAlarm valarm = createVAlarm();

        vevent.add(valarm);

        return vevent;
    }

    private VAlarm createVAlarm() {
        VAlarm valarm = (VAlarm) new VAlarm()
                .withProperty(new Action("DISPLAY"))
                .withProperty(new Trigger(Duration.ofMinutes(-15)))
                .withProperty(new Description("Reminder"))
                .getFluentTarget();
        return valarm;
    }

    private Calendar createCalendar(VTimeZone tz, VEvent vevent) {
        return new Calendar()
                .withProperty(new ProdId("-//FRAPPE//iCal4j 4.1.0//EN"))
                .withDefaults()
                .withProperty(new Method("REQUEST"))
                .withComponent(tz)
                .withComponent(vevent)
                .getFluentTarget();
    }

    public void processCalendar(InputStream is) {
        try {
            Calendar calendar = new CalendarBuilder().build(is);
            updateMeetingStatusFromICS(calendar);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateMeetingStatusFromICS(Calendar calendar) {
        for (Component component : calendar.getComponents(Component.VEVENT)) {
            VEvent event = (VEvent) component;
            String uid = event.getUid().orElseThrow().toString();
            Long meetingRequestId = extractMeetingId(uid);

            List<Attendee> attendees = event.getAttendees();
            for (Attendee attendee : attendees) {
                String email = attendee.getValue().replace("mailto:", "");
                PartStat responseStatus = (PartStat) attendee.getParameter(PartStat.PARTSTAT).orElseThrow();

                String status = mapPartStat(responseStatus);

                log.info("✅ Updating status for: {} → {}", email, status);
                meetingRequestService.updateMeetingRequestStatus(email, meetingRequestId, status);
            }
        }
    }

    private String mapPartStat(PartStat partStat) {
        if (partStat.equals(PartStat.ACCEPTED)) {
            return "ACCEPTED";
        } else if (partStat.equals(PartStat.DECLINED)) {
            return "DECLINED";
        } else if (partStat.equals(PartStat.TENTATIVE)) {
            return "TENTATIVE";
        }
        return "UNKNOWN";
    }

    private Long extractMeetingId(String uid) {
        Pattern pattern = Pattern.compile("frappe-(\\d+)");
        Matcher matcher = pattern.matcher(uid);

        if (matcher.find()) {
            return Long.parseLong(matcher.group(1)); // Extract numeric ID
        }
        return null; // If no match, return null
    }

}
