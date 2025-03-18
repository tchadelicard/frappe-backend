package fr.imt_atlantique.frappe.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import fr.imt_atlantique.frappe.dtos.CreateUserRequest;
import fr.imt_atlantique.frappe.dtos.LoginRequest;
import fr.imt_atlantique.frappe.dtos.LoginResponse;
import fr.imt_atlantique.frappe.entities.Student;
import fr.imt_atlantique.frappe.entities.User;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final StudentService studentService;
    private final JwtService jwtService;
    private final EmailService emailService;

    public AuthService(AuthenticationManager authenticationManager, UserService userService,
            StudentService studentService, JwtService jwtService,
            EmailService emailService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.studentService = studentService;
        this.jwtService = jwtService;
        this.emailService = emailService;
    }

    public void register(CreateUserRequest request) {
        Student student = studentService.createStudent(request);
        emailService.sendVerificationEmail(student);
    }

    public LoginResponse login(LoginRequest request) {
        // User user = userService.processLoginRequest(request);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user = (User) authentication.getPrincipal();

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
