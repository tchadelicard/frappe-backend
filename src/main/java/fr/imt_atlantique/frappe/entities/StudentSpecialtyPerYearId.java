package fr.imt_atlantique.frappe.entities;

import java.io.Serializable;
import java.util.Objects;

import org.hibernate.Hibernate;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class StudentSpecialtyPerYearId implements Serializable {
    private static final long serialVersionUID = 4502162861550193353L;
    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "specialty_id", nullable = false)
    private Long specialtyId;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        StudentSpecialtyPerYearId entity = (StudentSpecialtyPerYearId) o;
        return Objects.equals(this.studentId, entity.studentId) &&
                Objects.equals(this.specialtyId, entity.specialtyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, specialtyId);
    }

}