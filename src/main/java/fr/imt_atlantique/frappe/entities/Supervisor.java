package fr.imt_atlantique.frappe.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "supervisors")
public class Supervisor {
    @Id
    @Column(name = "supervisor_id", nullable = false)
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "supervisor_id", nullable = false)
    private User user;

    @Column(name = "meeting_url")
    private String meetingUrl;

    @Column(name = "caldav_username")
    private String caldavUsername;

    @Column(name = "caldav_password")
    private String caldavPassword;

    @OneToMany(mappedBy = "supervisor")
    private Set<MeetingRequest> meetingRequests = new LinkedHashSet<>();

}