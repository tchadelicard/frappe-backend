package fr.imt_atlantique.frappe.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "internship_requests")
public class InternshipRequest {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id", nullable = false)
    private MeetingRequest meetingRequests;

    @Column(name = "internship_duration", nullable = false)
    private Integer internshipDuration;

    @Column(name = "wanted_city", nullable = false)
    private String wantedCity;

    @Column(name = "wanted_country", nullable = false)
    private String wantedCountry;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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