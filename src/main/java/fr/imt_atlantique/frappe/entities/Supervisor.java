package fr.imt_atlantique.frappe.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.LinkedHashSet;
import java.util.Set;

@Setter
@Getter
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