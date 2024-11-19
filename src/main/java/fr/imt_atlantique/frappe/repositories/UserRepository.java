package fr.imt_atlantique.frappe.repositories;

import fr.imt_atlantique.frappe.entities.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User>   findByUsername(String username);
    Optional<User>  findByEmail(String email);
    User findByPhoneNumber(String phoneNumber);

}