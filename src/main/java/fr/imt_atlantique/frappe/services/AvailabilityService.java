package fr.imt_atlantique.frappe.services;

import fr.imt_atlantique.frappe.dtos.GetAvailableDaysRequest;
import fr.imt_atlantique.frappe.dtos.GetAvailableSlotsRequest;
import fr.imt_atlantique.frappe.entities.Supervisor;
import fr.imt_atlantique.frappe.repositories.SupervisorRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AvailabilityService {

    private final SupervisorRepository supervisorRepository;
    private final EncryptionService encryptionService;
    private final WebClient.Builder webClientBuilder;

    @Value("${frappe.availability.api.url}")
    private String availabilityApiUrl;

    public AvailabilityService(SupervisorRepository supervisorRepository, EncryptionService encryptionService, WebClient.Builder webClientBuilder) {
        this.supervisorRepository = supervisorRepository;
        this.encryptionService = encryptionService;
        this.webClientBuilder = webClientBuilder;
    }

    public ResponseEntity<?> getAvailableDaysForSupervisor(Long id, String duration) {
        Optional<Supervisor> supervisorOptional = supervisorRepository.findById(id);
        if (supervisorOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Supervisor not found"));
        }

        Supervisor supervisor = supervisorOptional.get();
        try {
            // Decrypt the CalDAV password
            String caldavPassword = encryptionService.decryptData(
                    supervisor.getCaldavPassword(),
                    supervisor.getCaldavPasswordSalt(),
                    supervisor.getCaldavPasswordIv()
            );

            // Build the request payload
            GetAvailableDaysRequest request = GetAvailableDaysRequest.builder()
                    .username(supervisor.getCaldavUsername())
                    .password(caldavPassword)
                    .duration(duration)
                    .build();

            // Use WebClient to POST and map response to List<String>
            Mono<List<String>> response = webClientBuilder.build()
                    .post()
                    .uri(availabilityApiUrl + "/days")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<String>>() {});

            List<String> availableDays = response.block();

            // Return the list of available days
            return ResponseEntity.ok(availableDays);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to process request", "details", e.getMessage()));
        }
    }

    public ResponseEntity<?> getAvailableSlotsForSupervisor(Long id, LocalDate date, String duration) {
        Optional<Supervisor> supervisorOptional = supervisorRepository.findById(id);
        if (supervisorOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Supervisor not found"));
        }

        Supervisor supervisor = supervisorOptional.get();
        try {
            // Decrypt the CalDAV password
            String caldavPassword = encryptionService.decryptData(
                    supervisor.getCaldavPassword(),
                    supervisor.getCaldavPasswordSalt(),
                    supervisor.getCaldavPasswordIv()
            );

            // Build the request payload
            GetAvailableSlotsRequest request = GetAvailableSlotsRequest.builder()
                    .username(supervisor.getCaldavUsername())
                    .password(caldavPassword)
                    .date(date)
                    .duration(duration)
                    .build();

            // Use WebClient to POST and map response to List<String>
            Mono<List<Map<String, Object>>> response = webClientBuilder.build()
                    .post()
                    .uri(availabilityApiUrl + "/slots")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {});

            List<Map<String, Object>> availableSlots = response.block();

            // Return the list of available slots
            return ResponseEntity.ok(availableSlots);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to process request", "details", e.getMessage()));
        }
    }
}
