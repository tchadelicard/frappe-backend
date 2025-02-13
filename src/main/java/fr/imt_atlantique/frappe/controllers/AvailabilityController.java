package fr.imt_atlantique.frappe.controllers;

import fr.imt_atlantique.frappe.services.AvailabilityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/availabilities")
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    public AvailabilityController(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    @GetMapping("/{id}/days")
    public ResponseEntity<List<LocalDate>> getAvailableDaysForSupervisor(
            @PathVariable Long id,
            @RequestParam String duration,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        List<LocalDate> availableDays = availabilityService.getAvailableDaysForSupervisor(id, duration, startDate, endDate);
        return ResponseEntity.ok(availableDays);
    }

    @GetMapping("/{id}/slots")
    public ResponseEntity<?> getAvailableSlotsForSupervisor(
            @PathVariable Long id,
            @RequestParam LocalDate date,
            @RequestParam String duration) {
        return ResponseEntity.ok(availabilityService.getAvailableSlotsForSupervisor(id, date, duration));
    }
}