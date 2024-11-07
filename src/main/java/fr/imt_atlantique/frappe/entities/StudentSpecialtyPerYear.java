package fr.imt_atlantique.frappe.entities;

import jakarta.persistence.*;

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

    public StudentSpecialtyPerYearId getId() {
        return id;
    }

    public void setId(StudentSpecialtyPerYearId id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    public void setSpecialty(Specialty specialty) {
        this.specialty = specialty;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

}