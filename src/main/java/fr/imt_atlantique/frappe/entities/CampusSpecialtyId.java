package fr.imt_atlantique.frappe.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class CampusSpecialtyId implements Serializable {
    private static final long serialVersionUID = 3211300009633235430L;
    @Column(name = "campus_id", nullable = false)
    private Long campusId;

    @Column(name = "specialty_id", nullable = false)
    private Long specialtyId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CampusSpecialtyId entity = (CampusSpecialtyId) o;
        return Objects.equals(this.specialtyId, entity.specialtyId) &&
                Objects.equals(this.campusId, entity.campusId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(specialtyId, campusId);
    }

}