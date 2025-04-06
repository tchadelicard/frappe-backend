package fr.imt_atlantique.frappe.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.imt_atlantique.frappe.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByValidationCode(String validationCode);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);
}