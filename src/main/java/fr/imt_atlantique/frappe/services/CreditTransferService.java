package fr.imt_atlantique.frappe.services;

import fr.imt_atlantique.frappe.dtos.CreditTransferDTO;
import fr.imt_atlantique.frappe.entities.CreditTransfer;
import fr.imt_atlantique.frappe.exceptions.CreditTransferNotFoundException;
import fr.imt_atlantique.frappe.repositories.CreditTransferRepository;
import fr.imt_atlantique.frappe.specifications.CreditTransferSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CreditTransferService {
    private final CreditTransferRepository creditTransferRepository;
    private final ModelMapper modelMapper;

    public CreditTransferService(CreditTransferRepository creditTransferRepository, ModelMapper modelMapper) {
        this.creditTransferRepository = creditTransferRepository;
        this.modelMapper = modelMapper;
    }

    public CreditTransferDTO toDTO(CreditTransfer creditTransfer) {
        return modelMapper.map(creditTransfer, CreditTransferDTO.class);
    }

    public List<CreditTransferDTO> toDTOs(List<CreditTransfer> creditTransfers) {
        return creditTransfers.stream()
                .map(creditTransfer -> modelMapper.map(creditTransfer, CreditTransferDTO.class))
                .toList();
    }

    public List<CreditTransfer> filterCreditTransfers(String university, String country, LocalDate startDate,
            LocalDate endDate) {
        Specification<CreditTransfer> spec = Specification
                .where(CreditTransferSpecification.hasUniversity(university))
                .and(CreditTransferSpecification.hasCountry(country))
                .and(CreditTransferSpecification.startDateAfter(startDate))
                .and(CreditTransferSpecification.endDateBefore(endDate));

        return creditTransferRepository.findAll(spec).stream()
                .toList();
    }

    public CreditTransfer getCreditTransferById(Long id) {
        return creditTransferRepository.findById(id)
                .orElseThrow(() -> new CreditTransferNotFoundException(Long.toString(id)));
    }
}
