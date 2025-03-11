package fr.imt_atlantique.frappe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import fr.imt_atlantique.frappe.entities.CreditTransfer;

public interface CreditTransferRepository
        extends JpaRepository<CreditTransfer, Long>, JpaSpecificationExecutor<CreditTransfer> {
}