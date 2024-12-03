package fr.imt_atlantique.frappe.services;

import fr.imt_atlantique.frappe.dtos.CreditTransferDTO;
import fr.imt_atlantique.frappe.entities.CreditTransfer;
import fr.imt_atlantique.frappe.repositories.CreditTransferRepository;
import fr.imt_atlantique.frappe.specifications.CreditTransferSpecification;
import jakarta.validation.constraints.Min;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CreditTransferService {
    private final CreditTransferRepository creditTransferRepository;
    private final ModelMapper modelMapper;

    public CreditTransferService(CreditTransferRepository creditTransferRepository, ModelMapper modelMapper) {
        this.creditTransferRepository = creditTransferRepository;
        this.modelMapper = modelMapper;
    }

    public ResponseEntity<List<CreditTransferDTO>> findByFilters(String university, String country, LocalDate startDate, LocalDate endDate) {
        Specification<CreditTransfer> spec = Specification
                .where(CreditTransferSpecification.hasUniversity(university))
                .and(CreditTransferSpecification.hasCountry(country))
                .and(CreditTransferSpecification.startDateAfter(startDate))
                .and(CreditTransferSpecification.endDateBefore(endDate));

        List<CreditTransfer> creditTransfers = creditTransferRepository.findAll(spec);

        if (creditTransfers.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<CreditTransferDTO> creditTransfersDTO = creditTransfers.stream()
                .map(creditTransfer -> modelMapper.map(creditTransfer, CreditTransferDTO.class))
                .toList();

        return ResponseEntity.ok(creditTransfersDTO);
    }

    public ResponseEntity<CreditTransferDTO> findById(@Min(1) Long id) {
        Optional<CreditTransfer> creditTransfer = creditTransferRepository.findById(id);
        if (creditTransfer.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        CreditTransferDTO creditTransferDTO = modelMapper.map(creditTransfer.get(), CreditTransferDTO.class);
        return ResponseEntity.ok(creditTransferDTO);
    }
}
