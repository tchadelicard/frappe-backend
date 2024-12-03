package fr.imt_atlantique.frappe.configs;

import fr.imt_atlantique.frappe.entities.Campus;
import fr.imt_atlantique.frappe.entities.CreditTransfer;
import fr.imt_atlantique.frappe.repositories.CampusRepository;
import fr.imt_atlantique.frappe.repositories.CreditTransferRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Configuration
public class DataInitializer {

    private final CampusRepository campusRepository;
    private final CreditTransferRepository creditTransferRepository;

    public DataInitializer(CampusRepository campusRepository, CreditTransferRepository creditTransferRepository) {
        this.campusRepository = campusRepository;
        this.creditTransferRepository = creditTransferRepository;
    }

    @Bean
    @Transactional
    public ApplicationRunner initializeCreditTransfers() {
        return args -> {
            loadCampuses();
            loadCreditTransfers();
        };
    }

    private void loadCampuses() {
        // Skip initialization if data already exists
        if (campusRepository.count() > 0) {
            return;
        }

        // Create and save mock Campus objects
        Campus campus1 = new Campus();
        campus1.setName("Brest");
        campusRepository.save(campus1);

        Campus campus2 = new Campus();
        campus2.setName("Rennes");
        campusRepository.save(campus2);

        Campus campus3 = new Campus();
        campus3.setName("Nantes");
        campusRepository.save(campus3);

        System.out.println("Campuses initialized successfully!");
    }

    private void loadCreditTransfers() {
        // Skip initialization if data already exists
        if (creditTransferRepository.count() > 0) {
            return;
        }

        // Create and save mock CreditTransfer objects
        CreditTransfer transfer1 = new CreditTransfer();
        transfer1.setUniversity("University of Oxford");
        transfer1.setCountry("UK");
        transfer1.setStartDate(LocalDate.of(2024, 1, 1));
        transfer1.setEndDate(LocalDate.of(2024, 6, 30));
        creditTransferRepository.save(transfer1);

        CreditTransfer transfer2 = new CreditTransfer();
        transfer2.setUniversity("Massachusetts Institute of Technology");
        transfer2.setCountry("USA");
        transfer2.setStartDate(LocalDate.of(2024, 2, 15));
        transfer2.setEndDate(LocalDate.of(2024, 8, 15));
        creditTransferRepository.save(transfer2);

        CreditTransfer transfer3 = new CreditTransfer();
        transfer3.setUniversity("École Polytechnique");
        transfer3.setCountry("France");
        transfer3.setStartDate(LocalDate.of(2023, 9, 1));
        transfer3.setEndDate(LocalDate.of(2024, 1, 31));
        creditTransferRepository.save(transfer3);

        CreditTransfer transfer4 = new CreditTransfer();
        transfer4.setUniversity("Technical University of Munich");
        transfer4.setCountry("Germany");
        transfer4.setStartDate(LocalDate.of(2023, 10, 1));
        transfer4.setEndDate(LocalDate.of(2024, 3, 31));
        creditTransferRepository.save(transfer4);

        CreditTransfer transfer5 = new CreditTransfer();
        transfer5.setUniversity("Kyoto University");
        transfer5.setCountry("Japan");
        transfer5.setStartDate(LocalDate.of(2024, 4, 1));
        transfer5.setEndDate(LocalDate.of(2024, 9, 30));
        creditTransferRepository.save(transfer5);

        System.out.println("Credit Transfers initialized successfully!");
   }
}
