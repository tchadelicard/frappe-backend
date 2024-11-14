package fr.imt_atlantique.frappe.services;

import fr.imt_atlantique.frappe.entities.Campus;
import fr.imt_atlantique.frappe.entities.User;
import fr.imt_atlantique.frappe.entities.validation;
import fr.imt_atlantique.frappe.repositories.CampusRepository;
import fr.imt_atlantique.frappe.repositories.UserRepository;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CampusRepository campusRepository; 

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    @Autowired
    private ValidationService validationService ;

    public User addUser(User user) {
        //Campus campus = campusRepository.findById(user.getCampus().getId())
        // .orElseThrow(() -> new IllegalArgumentException("Campus not found with id: " + user.getCampus().getId()));

      Campus campus = user.getCampus();
      if (campus.getId() == null || !campusRepository.existsById(campus.getId())) {
          campus = campusRepository.save(campus); // Save the new Campus entity
      }
        
       
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));

        user.setCampus(campus);
        user  = this.userRepository.save(user);

        this.validationService.register(user);

        return user;
    }

    public void activation (Map<String,String> activation) {
      validation validation =  this.validationService.readCode(activation.get("token"));
      if (Instant.now().isAfter(validation.getExpire())) {
        throw  new RuntimeException("Validation code expired");
      }
      
      User userActiv =  this.userRepository.findById(validation.getUser().getId()).orElseThrow(()-> new RuntimeException("User not found"));
      userActiv.setActive(true);
      this.userRepository.save(userActiv);
    }
}
