package fr.imt_atlantique.frappe.repositories;

import fr.imt_atlantique.frappe.entities.MeetingRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRequestRepository extends JpaRepository<MeetingRequest, Long> {
}
