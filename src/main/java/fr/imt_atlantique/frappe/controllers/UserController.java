package fr.imt_atlantique.frappe.controllers;

import fr.imt_atlantique.frappe.entities.User;
import fr.imt_atlantique.frappe.repositories.UserRepository;
import fr.imt_atlantique.frappe.services.testService;
import fr.imt_atlantique.frappe.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;




import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    private  UserService userService;


    @PostMapping("/inscription")
    public void inscription(@RequestBody User user) {
        log.info("Inscription de l'utilisateur : {}");
        this.userService.addUser(user);
    }

}
