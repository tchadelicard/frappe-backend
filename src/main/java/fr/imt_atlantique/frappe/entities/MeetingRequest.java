package fr.imt_atlantique.frappe.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

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

    @Column(name = "action_id")
    private Long actionId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "supervisor_id", nullable = false)
    private Supervisor supervisor;

    @OneToOne(mappedBy = "internshipRequest")
    private InternshipRequest internshipRequest;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRequestDescription() {
        return requestDescription;
    }

    public void setRequestDescription(String requestDescription) {
        this.requestDescription = requestDescription;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getActionId() {
        return actionId;
    }

    public void setActionId(Long actionId) {
        this.actionId = actionId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Supervisor getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Supervisor supervisor) {
        this.supervisor = supervisor;
    }

    public InternshipRequest getInternshipRequest() {
        return internshipRequest;
    }

    public void setInternshipRequest(InternshipRequest internshipRequest) {
        this.internshipRequest = internshipRequest;
    }

}