package fr.imt_atlantique.frappe.repositories;

import fr.imt_atlantique.frappe.entities.Campus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampusRepository extends JpaRepository<Campus, Long> {
  }