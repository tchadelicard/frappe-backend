package fr.imt_atlantique.frappe.services;

import fr.imt_atlantique.frappe.dtos.ActionDTO;
import fr.imt_atlantique.frappe.dtos.AvailabilitySlotDTO;
import fr.imt_atlantique.frappe.dtos.CreateMeetingRequestRequest;
import fr.imt_atlantique.frappe.dtos.MeetingRequestDTO;
import fr.imt_atlantique.frappe.entities.MeetingRequest;
import fr.imt_atlantique.frappe.entities.Student;
import fr.imt_atlantique.frappe.entities.Supervisor;
import fr.imt_atlantique.frappe.repositories.MeetingRequestRepository;
import fr.imt_atlantique.frappe.repositories.StudentRepository;
import fr.imt_atlantique.frappe.repositories.SupervisorRepository;
import jakarta.mail.*;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.search.FlagTerm;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.extern.slf4j.Slf4j;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.component.VAlarm;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.parameter.Cn;
import net.fortuna.ical4j.model.parameter.PartStat;
import net.fortuna.ical4j.model.parameter.Role;
import net.fortuna.ical4j.model.parameter.Rsvp;
import net.fortuna.ical4j.model.property.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class MeetingRequestService {

    private final MeetingRequestRepository meetingRequestRepository;
    private final StudentRepository studentRepository;
    private final SupervisorRepository supervisorRepository;
    private final JavaMailSender mailSender;
    private final AvailabilityService availabilityService;
    private final ModelMapper modelMapper;

    @Value("${frappe.mail.host}")
    private String host;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${frappe.mail.from}")
    private String from;

    public MeetingRequestService(MeetingRequestRepository meetingRequestRepository,
                                 StudentRepository studentRepository,
                                 SupervisorRepository supervisorRepository,
                                 JavaMailSender mailSender, AvailabilityService availabilityService, ModelMapper modelMapper) {
        this.meetingRequestRepository = meetingRequestRepository;
        this.studentRepository = studentRepository;
        this.supervisorRepository = supervisorRepository;
        this.mailSender = mailSender;
        this.availabilityService = availabilityService;
        this.modelMapper = modelMapper;
    }


    public MeetingRequestDTO createMeetingRequest(CreateMeetingRequestRequest request) throws MessagingException, IOException {
        if (!request.getStartDate().toLocalDate().equals(request.getEndDate().toLocalDate())) {
            throw new RuntimeException("Meeting must be on the same day");
        }

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Supervisor supervisor = supervisorRepository.findById(request.getSupervisorId())
                .orElseThrow(() -> new RuntimeException("Supervisor not found"));

        LocalDate date = request.getStartDate().toLocalDate();

        long minutes = ChronoUnit.MINUTES.between(request.getStartDate(), request.getEndDate());

        List<AvailabilitySlotDTO> availableSlots = availabilityService.getAvailableSlotsForSupervisor(supervisor.getId(), date, String.valueOf(minutes) + "m");

        boolean isSlotAvailable = availableSlots.stream()
                .anyMatch(slot -> slot.getStart().equals(request.getStartDate().atOffset(ZoneOffset.UTC)) &&
                        slot.getEnd().equals(request.getEndDate().atOffset(ZoneOffset.UTC)));

        if (!isSlotAvailable) {
            throw new RuntimeException("Slot is not available");
        }

        // Create and save the meeting request in the database
        MeetingRequest meetingRequest = new MeetingRequest();
        meetingRequest.setStartDate(request.getStartDate());
        meetingRequest.setEndDate(request.getEndDate());
        meetingRequest.setTheme(request.getTheme());
        meetingRequest.setLocation(request.getLocation());
        meetingRequest.setRequestDescription(request.getRequestDescription());
        meetingRequest.setStatus("PENDING");
        meetingRequest.setStudent(student);
        meetingRequest.setSupervisor(supervisor);

        meetingRequestRepository.save(meetingRequest);

        // Generate ICS file for the meeting
        String ics = generateICSEvent(meetingRequest);

        // Send meeting request email with ICS attachment
        sendMeetingInvitation(meetingRequest, ics);

        return modelMapper.map(meetingRequest, MeetingRequestDTO.class);
    }

    private String generateICSEvent(MeetingRequest meetingRequest) {
        TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
        VTimeZone tz = registry.getTimeZone("Europe/Paris").getVTimeZone();

        ZonedDateTime startDate = meetingRequest.getStartDate().atZone(ZoneId.of("UTC"))
                .withZoneSameInstant(ZoneId.of("Europe/Paris"));
        ZonedDateTime endDate = meetingRequest.getEndDate().atZone(ZoneId.of("UTC"))
                .withZoneSameInstant(ZoneId.of("Europe/Paris"));

        VEvent meeting = (VEvent) new VEvent(startDate, endDate, meetingRequest.getTheme())
                .withProperty(new Uid("frappe-" + meetingRequest.getId()))
                .withProperty(new Location(meetingRequest.getLocation()))
                .withProperty(new Description(meetingRequest.getRequestDescription()))
                .withProperty(new Attendee("mailto:" + meetingRequest.getStudent().getEmail())
                        .withParameter(new Cn(meetingRequest.getStudent().getFirstName() + " " + meetingRequest.getStudent().getLastName()))
                        .withParameter(Role.REQ_PARTICIPANT)
                        .withParameter(new PartStat("NEEDS-ACTION"))
                        .withParameter(new Rsvp(true))
                        .getFluentTarget())
                .withProperty(new Attendee("mailto:" + meetingRequest.getSupervisor().getEmail())
                        .withParameter(new Cn(meetingRequest.getSupervisor().getFirstName() + " " + meetingRequest.getSupervisor().getLastName()))
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

        VAlarm alarm = (VAlarm) new VAlarm()
                .withProperty(new Action("DISPLAY"))
                .withProperty(new Trigger(new Dur(0, 0, -15, 0)))
                .withProperty(new Description("Reminder"))
                .getFluentTarget();
        meeting.add(alarm);

        // Create calendar
        Calendar icsCalendar = new Calendar()
                .withProperty(new ProdId("-//FRAPPE//iCal4j 4.1.0//EN"))
                .withDefaults()
                .withProperty(new Method("REQUEST"))
                .withComponent(tz)
                .withComponent(meeting)
                .getFluentTarget();

        return icsCalendar.toString(); // Return the ICS content as a string
    }

    private void sendMeetingInvitation(MeetingRequest meetingRequest, String ics) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        // Generate ICS content
        ByteArrayInputStream inputStream = new ByteArrayInputStream(ics.getBytes(StandardCharsets.UTF_8));
        ByteArrayDataSource dataSource = new ByteArrayDataSource(inputStream, "text/calendar; charset=utf-8; method=REQUEST");

        // Email details
        helper.setFrom(from);
        helper.setTo(new String[]{meetingRequest.getStudent().getEmail(), meetingRequest.getSupervisor().getEmail()});
        helper.setSubject("Meeting Invitation: " + meetingRequest.getTheme());
        helper.setText("Dear Attendees,\n\nYou are invited to a meeting.\n\n" +
                "📅 **Theme:** " + meetingRequest.getTheme() + "\n" +
                "📍 **Location:** " + meetingRequest.getLocation() + "\n" +
                "🕒 **Date:** " + meetingRequest.getStartDate() + " - " + meetingRequest.getEndDate() + "\n\n" +
                "Please find the attached meeting invite.\n\nBest regards.");

        // Attach ICS as InputStream
        helper.addAttachment("meeting.ics", dataSource);

        // Send email
        mailSender.send(message);
        log.info("Meeting invitation sent successfully!");
    }

    @Scheduled(fixedRate = 50000) // Runs every 5 seconds
    public void checkMeetingResponses() {
        log.info("📨 Checking for new meeting responses...");
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imaps");

        try {
            // Connect to email server
            Session emailSession = Session.getDefaultInstance(properties);
            Store store = emailSession.getStore("imaps");
            store.connect(host, username, password);

            // Open Inbox
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);

            // Get unread messages with ICS attachments
            Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

            for (Message message : messages) {
                if (message.isMimeType("multipart/*")) {
                    Multipart multipart = (Multipart) message.getContent();

                    for (int i = 0; i < multipart.getCount(); i++) {
                        BodyPart part = multipart.getBodyPart(i);

                        // ✅ Check if Content-Type is "text/calendar"
                        if (part.isMimeType("text/calendar")) {
                            log.info("📩 Found calendar event (text/calendar)");
                            InputStream is = part.getInputStream();
                            Calendar calendar = new CalendarBuilder().build(is);
                            updateMeetingStatusFromICS(calendar);
                            message.setFlag(Flags.Flag.SEEN, true); // Mark email as read
                            continue;
                        }

                        // ✅ Check if attachment has a filename before calling `.endsWith(".ics")`
                        String fileName = part.getFileName();
                        if (fileName != null && fileName.toLowerCase().endsWith(".ics")) {
                            log.info("\uD83D\uDCE9 Found ICS file attachment: {}", fileName);
                            InputStream is = part.getInputStream();
                            Calendar calendar = new CalendarBuilder().build(is);
                            updateMeetingStatusFromICS(calendar);
                            message.setFlag(Flags.Flag.SEEN, true); // Mark email as read
                        }
                    }
                }
            }

            inbox.close(false);
            store.close();
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
                updateMeetingRequest(email, meetingRequestId, status);
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

    private void updateMeetingRequest(String email, Long meetingRequestId, String status) {
        MeetingRequest meetingRequest = meetingRequestRepository.findById(meetingRequestId)
                .orElseThrow(() -> new RuntimeException("Meeting request not found"));
        meetingRequest.setStatus(status);
        meetingRequestRepository.save(meetingRequest);
        log.info("✅ Meeting request updated for: {}", email);
    }

    private Long extractMeetingId(String uid) {
        Pattern pattern = Pattern.compile("frappe-(\\d+)");
        Matcher matcher = pattern.matcher(uid);

        if (matcher.find()) {
            return Long.parseLong(matcher.group(1)); // Extract numeric ID
        }
        return null; // If no match, return null
    }

    public ActionDTO getAction(Long id) {
        MeetingRequest meetingRequest = meetingRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Meeting request not found"));

        fr.imt_atlantique.frappe.entities.Action action = meetingRequest.getAction();
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
