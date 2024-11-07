package fr.imt_atlantique.frappe.entities;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "curriculums")
public class Curriculum {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "curriculums_id_gen")
    @SequenceGenerator(name = "curriculums_id_gen", sequenceName = "curriculums_curriculum_id_seq", allocationSize = 1)
    @Column(name = "curriculum_id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "curriculum")
    private Set<StudentCurriculum> studentCurriculums = new LinkedHashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<StudentCurriculum> getStudentsCurriculums() {
        return studentCurriculums;
    }

    public void setStudentsCurriculums(Set<StudentCurriculum> studentCurriculums) {
        this.studentCurriculums = studentCurriculums;
    }

}