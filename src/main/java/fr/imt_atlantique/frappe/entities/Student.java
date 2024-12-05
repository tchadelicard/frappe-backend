package fr.imt_atlantique.frappe.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "students")
@PrimaryKeyJoinColumn(name = "student_id")
public class Student extends User {
    @Column(name = "gender")
    private String gender;

    @Column(name = "nationality")
    private String nationality;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_transfer_id")
    private CreditTransfer creditTransfer;

    @OneToMany(mappedBy = "student")
    private Set<MeetingRequest> meetingRequests = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "students")
    private Set<Curriculum> curriculums = new LinkedHashSet<>();

    @OneToMany(mappedBy = "student")
    private Set<StudentSpecialtyPerYear> studentsSpecialtiesPerYears = new LinkedHashSet<>();

}