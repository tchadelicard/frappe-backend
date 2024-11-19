package fr.imt_atlantique.frappe.controllers;

import fr.imt_atlantique.AuthentificationDTO;
import fr.imt_atlantique.frappe.entities.User;
import fr.imt_atlantique.frappe.services.UserService;

import java.util.Map;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/inscription")
    public void inscription(@RequestBody User user) {
        log.info("Inscription de l'utilisateur : {}", user);
        this.userService.addUser(user);
    }

    @PostMapping("/activation")
    public void activation(@RequestBody Map<String, String> activation) {
        log.info("Activation de l'utilisateur");
        this.userService.activation(activation);
    }

    @PostMapping("/connexion")
    public Map<String, String> connexion(@RequestBody AuthentificationDTO authentificationDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authentificationDTO.email(), authentificationDTO.password())
            );
            log.info("Connexion de l'utilisateur : {}", authentication.isAuthenticated());
        } catch (Exception e) {
            log.error("Erreur lors de la connexion : {}", e.getMessage());
        }
        return Map.of("status", "failed");
    }
    
}
