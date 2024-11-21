package fr.imt_atlantique.frappe.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "meeting_requests")
public class MeetingRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "meeting_requests_id_gen")
    @SequenceGenerator(name = "meeting_requests_id_gen", sequenceName = "meeting_requests_meeting_request_id_seq", allocationSize = 1)
    @Column(name = "meeting_request_id", nullable = false)
    private Long id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "duration", nullable = false)
    private Integer duration;

    @Column(name = "theme", nullable = false)
    private String theme;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "request_description", nullable = false, length = Integer.MAX_VALUE)
    private String requestDescription;

    @Column(name = "status", nullable = false)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "action_id")
    private Action action;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "supervisor_id", nullable = false)
    private Supervisor supervisor;

    @OneToOne
    private InternshipRequest internshipRequest;

}