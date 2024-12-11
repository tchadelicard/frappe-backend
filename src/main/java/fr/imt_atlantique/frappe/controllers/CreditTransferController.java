package fr.imt_atlantique.frappe.controllers;

import fr.imt_atlantique.frappe.dtos.CreditTransferDTO;
import fr.imt_atlantique.frappe.entities.CreditTransfer;
import fr.imt_atlantique.frappe.services.CreditTransferService;
import jakarta.validation.constraints.Min;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/credit-transfers")
@CrossOrigin(origins = "http://localhost:5173")
public class CreditTransferController {

    private final CreditTransferService creditTransferService;

    public CreditTransferController(CreditTransferService creditTransferService) {
        this.creditTransferService = creditTransferService;
    }

    @GetMapping("/")
    public ResponseEntity<List<CreditTransferDTO>> getCreditTransfers(
            @RequestParam(required = false) String university,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return creditTransferService.findByFilters(university, country, startDate, endDate);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreditTransferDTO> getCreditTransfer(@PathVariable @Min(1) Long id) {
        return creditTransferService.findById(id);
    }
}