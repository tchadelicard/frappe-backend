package fr.imt_atlantique.frappe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.imt_atlantique.frappe.entities.MeetingRequest;

public interface MeetingRequestRepository extends JpaRepository<MeetingRequest, Long> {
}
