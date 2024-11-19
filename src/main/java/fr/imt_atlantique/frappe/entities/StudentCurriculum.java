package fr.imt_atlantique.frappe.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "students_curriculums")
public class StudentCurriculum {
    @SequenceGenerator(name = "students_curriculums_id_gen", sequenceName = "actions_id_seq", allocationSize = 1)
    @EmbeddedId
    private StudentCurriculumId id;

    @MapsId
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @MapsId
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "curriculum_id", nullable = false)
    private Curriculum curriculum;

    @Column(name = "year", nullable = false)
    private Integer year;

}