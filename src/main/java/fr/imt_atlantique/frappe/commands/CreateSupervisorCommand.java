package fr.imt_atlantique.frappe.commands;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import fr.imt_atlantique.frappe.dtos.CreateSupervisorRequest;
import fr.imt_atlantique.frappe.services.SupervisorService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CreateSupervisorCommand implements CommandLineRunner {

    private final SupervisorService supervisorService;
    private final Validator validator;

    public CreateSupervisorCommand(SupervisorService supervisorService, Validator validator) {
        this.supervisorService = supervisorService;
        this.validator = validator;
    }

    @Override
    public void run(String... args) {
        if (args.length < 7 || !args[0].equals("create-supervisor")) {
            log.error("❌ Incorrect usage! Expected format:");
            log.info("create-supervisor <username> <firstName> <lastName> <email> <password> <campusId>");
            return;
        }

        CreateSupervisorRequest request = new CreateSupervisorRequest();
        request.setUsername(args[1]);
        request.setFirstName(args[2]);
        request.setLastName(args[3]);
        request.setEmail(args[4]);
        request.setPassword(args[5]);

        try {
            request.setCampusId(Long.parseLong(args[6])); // Ensure campusId is a valid Long
        } catch (NumberFormatException e) {
            log.error("❌ Invalid campus ID. It must be a number.");
            return;
        }

        // ✅ Validate the DTO
        Set<ConstraintViolation<CreateSupervisorRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<CreateSupervisorRequest> violation : violations) {
                log.error("❌ Validation error: " + violation.getMessage());
            }
            return;
        }

        try {
            supervisorService.createSupervisor(request);
            log.info(
                    "✅ Supervisor " + request.getFirstName() + " " + request.getLastName() + " created successfully!");
        } catch (Exception e) {
            log.error("❌ Error creating supervisor: " + e.getMessage());
        }
    }
}