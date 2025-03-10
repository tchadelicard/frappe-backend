package fr.imt_atlantique.frappe.controllers;

import fr.imt_atlantique.frappe.dtos.CampusDTO;
import fr.imt_atlantique.frappe.services.CampusService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/campuses")
public class CampusController {
    private final CampusService campusService;

    public CampusController(CampusService campusService) {
        this.campusService = campusService;
    }

    @GetMapping
    public ResponseEntity<List<CampusDTO>> getCampuses() {
        List<CampusDTO> campuses = campusService.toDTOs(campusService.getCampuses());
        return ResponseEntity.ok(campuses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CampusDTO> getCampusById(@Valid @PathVariable @Min(1) Long id) {
        CampusDTO campus = campusService.toDTO(campusService.getCampusById(id));
        return ResponseEntity.ok(campus);
    }

}
