package fr.imt_atlantique.frappe.repositories;

import fr.imt_atlantique.frappe.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUsername(String name);
}