package fr.imt_atlantique.frappe.services;

import org.springframework.stereotype.Service;

import fr.imt_atlantique.frappe.dtos.LoginRequest;
import fr.imt_atlantique.frappe.dtos.LoginResponse;
import fr.imt_atlantique.frappe.dtos.RegistrationRequest;
import fr.imt_atlantique.frappe.entities.Student;
import fr.imt_atlantique.frappe.entities.User;

@Service
public class AuthService {

    private final UserService userService;
    private final StudentService studentService;
    private final JwtService jwtService;
    private final EmailService emailService;

    public AuthService(UserService userService, StudentService studentService, JwtService jwtService,
            EmailService emailService) {
        this.userService = userService;
        this.studentService = studentService;
        this.jwtService = jwtService;
        this.emailService = emailService;
    }

    public void register(RegistrationRequest request) {
        Student student = studentService.createStudent(request);
        emailService.sendVerificationEmail(student);
    }

    public LoginResponse login(LoginRequest request) {
        User user = userService.processLoginRequest(request);

        String token = jwtService.generateToken(user);
        return LoginResponse.builder()
                .token(token)
                .role(user.getAuthorities().iterator().next().getAuthority())
                .build();
    }

    public void verifyAccount(String validationCode) {
        userService.checkUserValidationCode(validationCode);
    }

    public void resendVerificationCode(String email) {
        User user = userService.resendValidationCode(email);
        emailService.sendVerificationEmail(user);
    }
}
