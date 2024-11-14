
package fr.imt_atlantique.frappe.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import fr.imt_atlantique.frappe.entities.validation;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class NotificationService {
    
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(validation validation) {
        log.info("Sending email to: {}", validation.getUser().getEmail());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no-Reply@Frape.com");
        message.setTo(validation.getUser().getEmail());
        message.setSubject("Validation");
        message.setText("Your validation token is: " + validation.getToken());
        
        javaMailSender.send(message);
    }

}
