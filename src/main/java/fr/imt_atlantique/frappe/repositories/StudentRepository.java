package fr.imt_atlantique.frappe.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.imt_atlantique.frappe.entities.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUsername(String name);
}