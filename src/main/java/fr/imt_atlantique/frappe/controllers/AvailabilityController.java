package fr.imt_atlantique.frappe.controllers;

import fr.imt_atlantique.frappe.services.AvailabilityService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;

@RestController
@RequestMapping("/availabilities")
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    public AvailabilityController(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    @GetMapping("/{id}/days")
    public ResponseEntity<?> getAvailableDaysForSupervisor(@PathVariable Long id, @RequestParam String duration) {
        return availabilityService.getAvailableDaysForSupervisor(id, duration);
    }

    @GetMapping("/{id}/slots")
    public ResponseEntity<?> getAvailableSlotsForSupervisor(@PathVariable Long id, @RequestParam LocalDate date, @RequestParam String duration) {
        return availabilityService.getAvailableSlotsForSupervisor(id, date, duration);
    }
}
