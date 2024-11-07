package fr.imt_atlantique.frappe.entities;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "specialties")
public class Specialty {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "specialties_id_gen")
    @SequenceGenerator(name = "specialties_id_gen", sequenceName = "specialties_specialty_id_seq", allocationSize = 1)
    @Column(name = "specialty_id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(mappedBy = "specialties")
    private Set<Campus> campuses = new LinkedHashSet<>();

    @OneToMany(mappedBy = "specialty")
    private Set<StudentSpecialtyPerYear> studentsSpecialtiesPerYears = new LinkedHashSet<>();

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

    public Set<Campus> getCampuses() {
        return campuses;
    }

    public void setCampuses(Set<Campus> campuses) {
        this.campuses = campuses;
    }

    public Set<StudentSpecialtyPerYear> getStudentsSpecialtiesPerYears() {
        return studentsSpecialtiesPerYears;
    }

    public void setStudentsSpecialtiesPerYears(Set<StudentSpecialtyPerYear> studentsSpecialtiesPerYears) {
        this.studentsSpecialtiesPerYears = studentsSpecialtiesPerYears;
    }

}