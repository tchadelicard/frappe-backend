package fr.imt_atlantique.frappe.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import fr.imt_atlantique.frappe.entities.MeetingRequest;
import fr.imt_atlantique.frappe.entities.User;
import fr.imt_atlantique.frappe.events.MeetingRequestResponseEvent;
import fr.imt_atlantique.frappe.exceptions.FailedToSendEmailException;
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
    private final ApplicationEventPublisher eventPublisher;

    public EmailService(JavaMailSender mailSender, ApplicationEventPublisher eventPublisher) {
        this.mailSender = mailSender;
        this.eventPublisher = eventPublisher;
    }

    public void sendVerificationEmail(User user) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(user.getEmail());
            helper.setSubject("Verify Your Account");
            helper.setText("Validation code: " + user.getValidationCode());
            mailSender.send(message);
            log.info("Validation code sent successfully!");
        } catch (MessagingException e) {
            throw new FailedToSendEmailException("Error sending verification email.");
        }
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

    @Scheduled(fixedRate = 600000) // Runs every 5 seconds
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
                            eventPublisher.publishEvent(new MeetingRequestResponseEvent(is));
                            message.setFlag(Flags.Flag.SEEN, true); // Mark email as read
                            continue;
                        }

                        // ✅ Check if attachment has a filename before calling `.endsWith(".ics")`
                        String fileName = part.getFileName();
                        if (fileName != null && fileName.toLowerCase().endsWith(".ics")) {
                            log.info("\uD83D\uDCE9 Found ICS file attachment: {}", fileName);
                            InputStream is = part.getInputStream();
                            eventPublisher.publishEvent(new MeetingRequestResponseEvent(is));
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

}
