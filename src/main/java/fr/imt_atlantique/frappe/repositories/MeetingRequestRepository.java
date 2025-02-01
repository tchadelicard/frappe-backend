package fr.imt_atlantique.frappe.repositories;

import fr.imt_atlantique.frappe.entities.MeetingRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingRequestRepository extends JpaRepository<MeetingRequest, Long> {
}
