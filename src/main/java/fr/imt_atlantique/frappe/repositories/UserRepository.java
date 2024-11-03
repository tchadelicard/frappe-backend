package fr.imt_atlantique.frappe.repositories;

import fr.imt_atlantique.frappe.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);

}