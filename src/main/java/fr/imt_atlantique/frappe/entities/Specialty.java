package fr.imt_atlantique.frappe.entities;

import java.util.LinkedHashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

}