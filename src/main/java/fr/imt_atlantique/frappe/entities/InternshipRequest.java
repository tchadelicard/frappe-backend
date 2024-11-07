package fr.imt_atlantique.frappe.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

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
    private MeetingRequest meetingRequests;

    @Column(name = "internship_duration", nullable = false)
    private Integer internshipDuration;

    @Column(name = "wanted_city", nullable = false)
    private String wantedCity;

    @Column(name = "wanted_country", nullable = false)
    private String wantedCountry;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MeetingRequest getMeetingRequests() {
        return meetingRequests;
    }

    public void setMeetingRequests(MeetingRequest meetingRequests) {
        this.meetingRequests = meetingRequests;
    }

    public Integer getInternshipDuration() {
        return internshipDuration;
    }

    public void setInternshipDuration(Integer internshipDuration) {
        this.internshipDuration = internshipDuration;
    }

    public String getWantedCity() {
        return wantedCity;
    }

    public void setWantedCity(String wantedCity) {
        this.wantedCity = wantedCity;
    }

    public String getWantedCountry() {
        return wantedCountry;
    }

    public void setWantedCountry(String wantedCountry) {
        this.wantedCountry = wantedCountry;
    }

}