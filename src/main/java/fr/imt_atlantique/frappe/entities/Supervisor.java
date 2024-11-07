package fr.imt_atlantique.frappe.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "supervisors")
public class Supervisor {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "supervisors_id_gen")
    @SequenceGenerator(name = "supervisors_id_gen", sequenceName = "supervisors_supervisor_id_seq", allocationSize = 1)
    @Column(name = "supervisor_id", nullable = false)
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @ColumnDefault("nextval('supervisors_supervisor_id_seq')")
    @JoinColumn(name = "supervisor_id", nullable = false)
    private User users;

    @Column(name = "meeting_url")
    private String meetingUrl;

    @Column(name = "caldav_username")
    private String caldavUsername;

    @Column(name = "caldav_password")
    private String caldavPassword;

    @OneToMany(mappedBy = "supervisor")
    private Set<MeetingRequest> meetingRequests = new LinkedHashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUsers() {
        return users;
    }

    public void setUsers(User users) {
        this.users = users;
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

    public Set<MeetingRequest> getMeetingRequests() {
        return meetingRequests;
    }

    public void setMeetingRequests(Set<MeetingRequest> meetingRequests) {
        this.meetingRequests = meetingRequests;
    }

}