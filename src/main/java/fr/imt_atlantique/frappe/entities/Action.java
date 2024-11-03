package fr.imt_atlantique.frappe.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "actions")
public class Action {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "actions_id_gen")
    @SequenceGenerator(name = "actions_id_gen", sequenceName = "actions_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "notes", nullable = false, length = Integer.MAX_VALUE)
    private String notes;

    @Column(name = "action_plan", nullable = false, length = Integer.MAX_VALUE)
    private String actionPlan;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "meeting_request_id", nullable = false)
    private MeetingRequest meetingRequest;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getActionPlan() {
        return actionPlan;
    }

    public void setActionPlan(String actionPlan) {
        this.actionPlan = actionPlan;
    }

    public MeetingRequest getMeetingRequest() {
        return meetingRequest;
    }

    public void setMeetingRequest(MeetingRequest meetingRequest) {
        this.meetingRequest = meetingRequest;
    }

}