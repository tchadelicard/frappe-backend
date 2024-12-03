package fr.imt_atlantique.frappe.services;

import fr.imt_atlantique.frappe.dtos.CampusDTO;
import fr.imt_atlantique.frappe.entities.Campus;
import fr.imt_atlantique.frappe.repositories.CampusRepository;
import jakarta.validation.constraints.Min;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CampusService {
    private final CampusRepository campusRepository;
    private final ModelMapper modelMapper;

    public CampusService(CampusRepository campusRepository, ModelMapper modelMapper) {
        this.campusRepository = campusRepository;
        this.modelMapper = modelMapper;
    }

    public ResponseEntity<List<CampusDTO>> getAllCampuses() {
        List<Campus> campuses = campusRepository.findAll();
        List<CampusDTO> campusesDTO = campuses.stream()
                .map(campus ->  modelMapper.map(campus, CampusDTO.class))
                .toList();
        return ResponseEntity.ok(campusesDTO);
    }

    public ResponseEntity<CampusDTO> getCampusById(@Min(1) Long id) {
        Optional<Campus> campus = campusRepository.findById(id);
        if (campus.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        CampusDTO campusDTO = modelMapper.map(campus.get(), CampusDTO.class);
        return ResponseEntity.ok(campusDTO);
    }

}
