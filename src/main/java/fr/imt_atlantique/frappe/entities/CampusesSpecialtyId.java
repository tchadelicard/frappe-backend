package fr.imt_atlantique.frappe.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CampusesSpecialtyId implements Serializable {
    private static final long serialVersionUID = 4148543775186802109L;
    @Column(name = "campus_id", nullable = false)
    private Integer campusId;

    @Column(name = "specialty_id", nullable = false)
    private Integer specialtyId;

    public Integer getCampusId() {
        return campusId;
    }

    public void setCampusId(Integer campusId) {
        this.campusId = campusId;
    }

    public Integer getSpecialtyId() {
        return specialtyId;
    }

    public void setSpecialtyId(Integer specialtyId) {
        this.specialtyId = specialtyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CampusesSpecialtyId entity = (CampusesSpecialtyId) o;
        return Objects.equals(this.specialtyId, entity.specialtyId) &&
                Objects.equals(this.campusId, entity.campusId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(specialtyId, campusId);
    }

}