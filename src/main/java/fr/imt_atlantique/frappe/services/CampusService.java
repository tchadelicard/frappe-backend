package fr.imt_atlantique.frappe.services;

import fr.imt_atlantique.frappe.dtos.CampusDTO;
import fr.imt_atlantique.frappe.entities.Campus;
import fr.imt_atlantique.frappe.exceptions.CampusNotFoundException;
import fr.imt_atlantique.frappe.repositories.CampusRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CampusService {
    private final CampusRepository campusRepository;
    private final ModelMapper modelMapper;

    public CampusService(CampusRepository campusRepository, ModelMapper modelMapper) {
        this.campusRepository = campusRepository;
        this.modelMapper = modelMapper;
    }

    public List<CampusDTO> getCampuses() {
        List<Campus> campuses = campusRepository.findAll();
        return campuses.stream()
                .map(campus -> modelMapper.map(campus, CampusDTO.class))
                .toList();
    }

    public CampusDTO getCampusById(@Valid @Min(1) Long id) {
        return campusRepository.findById(id)
                .map(campus -> modelMapper.map(campus, CampusDTO.class))
                .orElseThrow(() -> new CampusNotFoundException(Long.toString(id)));
    }

}
