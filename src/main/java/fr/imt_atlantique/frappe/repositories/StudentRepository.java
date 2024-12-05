package fr.imt_atlantique.frappe.repositories;

import fr.imt_atlantique.frappe.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}