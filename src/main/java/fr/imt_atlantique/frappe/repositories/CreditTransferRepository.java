package fr.imt_atlantique.frappe.repositories;

import fr.imt_atlantique.frappe.entities.CreditTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CreditTransferRepository extends JpaRepository<CreditTransfer, Long>, JpaSpecificationExecutor<CreditTransfer> {
}