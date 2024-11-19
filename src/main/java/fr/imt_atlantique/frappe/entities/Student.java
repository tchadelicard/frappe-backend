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
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "students_id_gen")
    @SequenceGenerator(name = "students_id_gen", sequenceName = "students_student_id_seq", allocationSize = 1)
    @Column(name = "student_id", nullable = false)
    private Long id;

    @Setter
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

}