package fr.imt_atlantique.frappe.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "campuses")
public class Campus {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "campuses_id_gen")
    @SequenceGenerator(name = "campuses_id_gen", sequenceName = "campuses_campus_id_seq", allocationSize = 1)
    @Column(name = "campus_id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany
    @JoinTable(joinColumns = @JoinColumn(name = "campus_id"),
            inverseJoinColumns = @JoinColumn(name = "specialty_id"))
    private Set<Specialty> specialties = new LinkedHashSet<>();

    @OneToMany(mappedBy = "campus")
    private Set<User> users = new LinkedHashSet<>();

}