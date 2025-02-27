package fr.imt_atlantique.frappe.services;

import fr.imt_atlantique.frappe.dtos.AvailabilitySlotDTO;
import fr.imt_atlantique.frappe.dtos.GetAvailableDaysRequest;
import fr.imt_atlantique.frappe.dtos.GetAvailableSlotsRequest;
import fr.imt_atlantique.frappe.entities.Supervisor;
import fr.imt_atlantique.frappe.exceptions.SupervisorNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.List;

@Service
public class AvailabilityService {

    private final SupervisorService supervisorService;
    private final EncryptionService encryptionService;
    private final WebClient.Builder webClientBuilder;

    @Value("${frappe.availability.api.url}")
    private String availabilityApiUrl;

    public AvailabilityService(SupervisorService supervisorService, EncryptionService encryptionService,
            WebClient.Builder webClientBuilder) {
        this.supervisorService = supervisorService;
        this.encryptionService = encryptionService;
        this.webClientBuilder = webClientBuilder;
    }

    public List<LocalDate> getAvailableDaysForSupervisor(Long id, String duration, LocalDate startDate,
            LocalDate endDate) {
        Supervisor supervisor = supervisorService.getSupervisorById(id);

        String caldavPassword = getCalDAVPassword(supervisor);

        GetAvailableDaysRequest request = GetAvailableDaysRequest.builder()
                .username(supervisor.getCaldavUsername())
                .password(caldavPassword)
                .duration(duration)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        return webClientBuilder.build()
                .post()
                .uri(availabilityApiUrl + "/days")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<LocalDate>>() {
                })
                .block();
    }

    public List<AvailabilitySlotDTO> getAvailableSlotsForSupervisor(Long id, LocalDate date, String duration) {
        Supervisor supervisor = supervisorService.getSupervisorById(id);

        String caldavPassword = getCalDAVPassword(supervisor);

        GetAvailableSlotsRequest request = GetAvailableSlotsRequest.builder()
                .username(supervisor.getCaldavUsername())
                .password(caldavPassword)
                .date(date)
                .duration(duration)
                .build();

        return webClientBuilder.build()
                .post()
                .uri(availabilityApiUrl + "/slots")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<AvailabilitySlotDTO>>() {
                })
                .block();
    }

    private String getCalDAVPassword(Supervisor supervisor) {
        try {
            return encryptionService.decryptData(
                    supervisor.getCaldavPassword(),
                    supervisor.getCaldavPasswordSalt(),
                    supervisor.getCaldavPasswordIv());
        } catch (Exception e) {
            throw new SupervisorNotFoundException("Supervisor not found");
        }
    }
}
