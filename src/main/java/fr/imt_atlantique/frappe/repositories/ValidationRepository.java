package fr.imt_atlantique.frappe.repositories;

import org.springframework.data.repository.CrudRepository;

import fr.imt_atlantique.frappe.entities.validation;
import java.util.List;
import java.util.Optional;


public interface ValidationRepository extends CrudRepository<validation, Integer> {
    

    Optional<validation>  findByToken(String token);
}
