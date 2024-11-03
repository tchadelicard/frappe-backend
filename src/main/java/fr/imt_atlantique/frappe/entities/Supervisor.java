package fr.imt_atlantique.frappe.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "supervisors")
public class Supervisor {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id", nullable = false)
    private User user;

    @Column(name = "meeting_url")
    private String meetingUrl;

    @Column(name = "caldav_username")
    private String caldavUsername;

    @Column(name = "caldav_password")
    private String caldavPassword;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMeetingUrl() {
        return meetingUrl;
    }

    public void setMeetingUrl(String meetingUrl) {
        this.meetingUrl = meetingUrl;
    }

    public String getCaldavUsername() {
        return caldavUsername;
    }

    public void setCaldavUsername(String caldavUsername) {
        this.caldavUsername = caldavUsername;
    }

    public String getCaldavPassword() {
        return caldavPassword;
    }

    public void setCaldavPassword(String caldavPassword) {
        this.caldavPassword = caldavPassword;
    }

}