package fr.imt_atlantique.frappe.controllers;

import fr.imt_atlantique.frappe.dtos.CreditTransferDTO;
import fr.imt_atlantique.frappe.services.CreditTransferService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/credit-transfers")
public class CreditTransferController {

    private final CreditTransferService creditTransferService;

    public CreditTransferController(CreditTransferService creditTransferService) {
        this.creditTransferService = creditTransferService;
    }

    @GetMapping
    public ResponseEntity<List<CreditTransferDTO>> getCreditTransfers(
            @Valid @RequestParam(required = false) String university,
            @Valid @RequestParam(required = false) String country,
            @Valid @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Valid @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<CreditTransferDTO> creditTransfers = creditTransferService.findByFilters(university, country, startDate,
                endDate);
        return ResponseEntity.ok(creditTransfers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreditTransferDTO> getCreditTransfer(@Valid @PathVariable @Min(1) Long id) {
        CreditTransferDTO creditTransfer = creditTransferService.findById(id);
        return ResponseEntity.ok(creditTransfer);
    }
}