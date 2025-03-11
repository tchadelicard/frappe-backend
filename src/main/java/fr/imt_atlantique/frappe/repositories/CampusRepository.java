package fr.imt_atlantique.frappe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.imt_atlantique.frappe.entities.Campus;

public interface CampusRepository extends JpaRepository<Campus, Long> {
}