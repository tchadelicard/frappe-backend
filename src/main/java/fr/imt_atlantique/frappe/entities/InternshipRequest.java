package fr.imt_atlantique.frappe.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "internship_requests")
@PrimaryKeyJoinColumn(name = "internship_request_id")
public class InternshipRequest extends MeetingRequest {
    @Column(name = "internship_duration", nullable = false)
    private Integer internshipDuration;

    @Column(name = "wanted_city", nullable = false)
    private String wantedCity;

    @Column(name = "wanted_country", nullable = false)
    private String wantedCountry;

}