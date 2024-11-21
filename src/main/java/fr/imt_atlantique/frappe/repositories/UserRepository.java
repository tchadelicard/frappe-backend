package fr.imt_atlantique.frappe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.imt_atlantique.frappe.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByValidationCode(String validationCode);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}