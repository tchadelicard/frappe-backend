package fr.imt_atlantique.frappe.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "students_specialties_per_year")
public class StudentSpecialtyPerYear {
    @SequenceGenerator(name = "students_specialties_per_year_id_gen", sequenceName = "actions_id_seq", allocationSize = 1)
    @EmbeddedId
    private StudentSpecialtyPerYearId id;

    @MapsId
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @MapsId
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "specialty_id", nullable = false)
    private Specialty specialty;

    @Column(name = "year", nullable = false)
    private Integer year;

}