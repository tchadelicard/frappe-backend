package fr.imt_atlantique.frappe.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import fr.imt_atlantique.frappe.entities.MeetingRequest;
import fr.imt_atlantique.frappe.repositories.MeetingRequestRepository;
import jakarta.mail.BodyPart;
import jakarta.mail.Flags;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.search.FlagTerm;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.extern.slf4j.Slf4j;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.parameter.PartStat;
import net.fortuna.ical4j.model.property.Attendee;

@Service
@Slf4j
public class EmailService {
    @Value("${frappe.mail.host}")
    private String host;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${frappe.mail.from}")
    private String from;

    private final JavaMailSender mailSender;
    private final MeetingRequestRepository meetingRequestRepository;

    public EmailService(JavaMailSender mailSender, MeetingRequestRepository meetingRequestRepository) {
        this.mailSender = mailSender;
        this.meetingRequestRepository = meetingRequestRepository;
    }

    public void sendMeetingInvitation(MeetingRequest meetingRequest, String ics)
            throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        // Generate ICS content
        ByteArrayInputStream inputStream = new ByteArrayInputStream(ics.getBytes(StandardCharsets.UTF_8));
        ByteArrayDataSource dataSource = new ByteArrayDataSource(inputStream,
                "text/calendar; charset=utf-8; method=REQUEST");

        // Email details
        helper.setFrom(from);
        helper.setTo(
                new String[] { meetingRequest.getStudent().getEmail(), meetingRequest.getSupervisor().getEmail() });
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

}
