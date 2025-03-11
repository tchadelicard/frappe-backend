package fr.imt_atlantique.frappe.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.imt_atlantique.frappe.entities.Supervisor;

public interface SupervisorRepository extends JpaRepository<Supervisor, Long> {
    Optional<Supervisor> findByUsername(String name);
}
