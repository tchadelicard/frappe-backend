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
public class StudentSpecialtyPerYearId implements Serializable {
    private static final long serialVersionUID = -7300913457640451428L;
    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "specialty_id", nullable = false)
    private Long specialtyId;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        StudentSpecialtyPerYearId entity = (StudentSpecialtyPerYearId) o;
        return Objects.equals(this.studentId, entity.studentId) &&
                Objects.equals(this.year, entity.year) &&
                Objects.equals(this.specialtyId, entity.specialtyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, year, specialtyId);
    }

}