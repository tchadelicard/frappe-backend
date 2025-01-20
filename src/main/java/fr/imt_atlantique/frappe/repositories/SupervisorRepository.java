package fr.imt_atlantique.frappe.repositories;

import fr.imt_atlantique.frappe.entities.Supervisor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupervisorRepository extends JpaRepository<Supervisor, Long> {
}
