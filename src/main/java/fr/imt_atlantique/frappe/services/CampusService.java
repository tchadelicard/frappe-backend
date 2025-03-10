package fr.imt_atlantique.frappe.services;

import fr.imt_atlantique.frappe.dtos.CampusDTO;
import fr.imt_atlantique.frappe.entities.Campus;
import fr.imt_atlantique.frappe.exceptions.CampusNotFoundException;
import fr.imt_atlantique.frappe.repositories.CampusRepository;
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

    public CampusDTO toDTO(Campus campus) {
        return modelMapper.map(campus, CampusDTO.class);
    }

    public List<CampusDTO> toDTOs(List<Campus> campuses) {
        return campuses.stream()
                .map(campus -> modelMapper.map(campus, CampusDTO.class))
                .toList();
    }

    public List<Campus> getCampuses() {
        return campusRepository.findAll();
    }

    public Campus getCampusById(Long id) {
        return campusRepository.findById(id)
                .orElseThrow(() -> new CampusNotFoundException(Long.toString(id)));
    }
}
