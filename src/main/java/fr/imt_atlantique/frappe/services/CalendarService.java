package fr.imt_atlantique.frappe.services;

import fr.imt_atlantique.frappe.entities.MeetingRequest;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.component.VAlarm;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.parameter.Cn;

import net.fortuna.ical4j.model.parameter.PartStat;
import net.fortuna.ical4j.model.parameter.Role;
import net.fortuna.ical4j.model.parameter.Rsvp;
import net.fortuna.ical4j.model.property.*;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class CalendarService {
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
                .withProperty(new Trigger(new Dur(0, 0, -15, 0)))
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

}
