package fr.imt_atlantique.frappe.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "students_id_gen")
    @SequenceGenerator(name = "students_id_gen", sequenceName = "students_student_id_seq", allocationSize = 1)
    @Column(name = "student_id", nullable = false)
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @ColumnDefault("nextval('students_student_id_seq')")
    @JoinColumn(name = "student_id", nullable = false)
    private User user;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "nationality", nullable = false)
    private String nationality;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_transfer_id")
    private CreditTransfer creditTransfer;

    @OneToMany(mappedBy = "student")
    private Set<MeetingRequest> meetingRequests = new LinkedHashSet<>();

    @OneToMany(mappedBy = "student")
    private Set<StudentCurriculum> studentCurriculums = new LinkedHashSet<>();

    @OneToMany(mappedBy = "student")
    private Set<StudentSpecialtyPerYear> studentsSpecialtiesPerYears = new LinkedHashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public CreditTransfer getCreditTransfer() {
        return creditTransfer;
    }

    public void setCreditTransfer(CreditTransfer creditTransfer) {
        this.creditTransfer = creditTransfer;
    }

    public Set<MeetingRequest> getMeetingRequests() {
        return meetingRequests;
    }

    public void setMeetingRequests(Set<MeetingRequest> meetingRequests) {
        this.meetingRequests = meetingRequests;
    }

    public Set<StudentCurriculum> getStudentsCurriculums() {
        return studentCurriculums;
    }

    public void setStudentsCurriculums(Set<StudentCurriculum> studentCurriculums) {
        this.studentCurriculums = studentCurriculums;
    }

    public Set<StudentSpecialtyPerYear> getStudentsSpecialtiesPerYears() {
        return studentsSpecialtiesPerYears;
    }

    public void setStudentsSpecialtiesPerYears(Set<StudentSpecialtyPerYear> studentsSpecialtiesPerYears) {
        this.studentsSpecialtiesPerYears = studentsSpecialtiesPerYears;
    }

}