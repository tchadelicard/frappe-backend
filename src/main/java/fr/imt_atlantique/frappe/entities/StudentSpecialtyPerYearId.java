package fr.imt_atlantique.frappe.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class StudentSpecialtyPerYearId implements Serializable {
    private static final long serialVersionUID = 5118165665283465161L;
    @Column(name = "student_id", nullable = false)
    private Integer studentId;

    @Column(name = "specialty_id", nullable = false)
    private Integer specialtyId;

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
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
        StudentSpecialtyPerYearId entity = (StudentSpecialtyPerYearId) o;
        return Objects.equals(this.studentId, entity.studentId) &&
                Objects.equals(this.specialtyId, entity.specialtyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, specialtyId);
    }

}