package fr.imt_atlantique.frappe.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

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
    private Set<StudentSpecialtyPerYear> studentSpecialtyPerYears = new LinkedHashSet<>();

}