package fr.imt_atlantique.frappe.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "campuses_specialties")
public class CampusSpecialty {
    @SequenceGenerator(name = "campuses_specialties_id_gen", sequenceName = "campuses_campus_id_seq", allocationSize = 1)
    @EmbeddedId
    private CampusSpecialtyId id;

    @MapsId("campusId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "campus_id", nullable = false)
    private Campus campus;

    @MapsId("specialtyId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "specialty_id", nullable = false)
    private Specialty specialty;

}