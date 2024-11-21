package fr.imt_atlantique.frappe.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
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

}