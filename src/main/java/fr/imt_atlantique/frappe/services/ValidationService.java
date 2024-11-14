package fr.imt_atlantique.frappe.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.imt_atlantique.frappe.entities.User;
import fr.imt_atlantique.frappe.entities.validation;
import fr.imt_atlantique.frappe.repositories.ValidationRepository;

@Service
public class ValidationService {

    @Autowired
    private ValidationRepository validationRepository;
    
    @Autowired
    private NotificationService notificationService;

    

    public void register  ( User user) {
        validation validation = new validation();
        validation.setUser(user);
        Instant now = Instant.now();
        Instant expiration = Instant.now();
        expiration = expiration.plus(10, ChronoUnit.MINUTES);

        Random random = new Random();
        int randomInt = random.nextInt(1000000);
        String token = String.format("%06d", randomInt);
        validation.setToken(token);
        validation.setCreation(now);
        validation.setExpire(expiration);
        this.notificationService.sendEmail(validation);
        this.validationRepository.save(validation);
      
    }
    public validation readCode(String token){
      return   this.validationRepository.findByToken(token).orElseThrow(()-> new RuntimeException("votre code est invalide") );
    }
    

}
