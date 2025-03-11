package fr.imt_atlantique.frappe.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.imt_atlantique.frappe.dtos.ChangePasswordRequest;
import fr.imt_atlantique.frappe.dtos.ChangePasswordResponse;
import fr.imt_atlantique.frappe.dtos.LoginRequest;
import fr.imt_atlantique.frappe.dtos.LoginResponse;
import fr.imt_atlantique.frappe.dtos.RegistrationRequest;
import fr.imt_atlantique.frappe.dtos.RegistrationResponse;
import fr.imt_atlantique.frappe.dtos.ResendRequest;
import fr.imt_atlantique.frappe.dtos.ResendResponse;
import fr.imt_atlantique.frappe.dtos.VerifyRequest;
import fr.imt_atlantique.frappe.dtos.VerifyResponse;
import fr.imt_atlantique.frappe.services.AuthService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@RequestBody RegistrationRequest request) {
        log.info("Received registration request: {}");
        RegistrationResponse response = authService.register(request);
        if (response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        if (response.getToken() != null) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<VerifyResponse> verifyAccount(@RequestBody VerifyRequest request) {
        VerifyResponse response = authService.verifyAccount(request.getToken());
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @PostMapping("/resend")
    public ResponseEntity<ResendResponse> resendVerificationCode(@RequestBody ResendRequest request) {
        ResendResponse response = authService.resendVerificationCode(request.getEmail());
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @PostMapping("/change-password")
    public ResponseEntity<ChangePasswordResponse> changePassword(@RequestBody ChangePasswordRequest request) {
        ChangePasswordResponse response = authService.changePassword(request);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

}
