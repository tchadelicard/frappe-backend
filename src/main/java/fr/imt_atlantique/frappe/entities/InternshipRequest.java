package fr.imt_atlantique.frappe.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Setter
@Getter
@Entity
@Table(name = "internship_requests")
public class InternshipRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "internship_requests_id_gen")
    @SequenceGenerator(name = "internship_requests_id_gen", sequenceName = "internship_requests_internship_request_id_seq", allocationSize = 1)
    @Column(name = "internship_request_id", nullable = false)
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @ColumnDefault("nextval('internship_requests_internship_request_id_seq')")
    @JoinColumn(name = "internship_request_id", nullable = false)
    private MeetingRequest meetingRequest;

    @Column(name = "internship_duration", nullable = false)
    private Integer internshipDuration;

    @Column(name = "wanted_city", nullable = false)
    private String wantedCity;

    @Column(name = "wanted_country", nullable = false)
    private String wantedCountry;

}