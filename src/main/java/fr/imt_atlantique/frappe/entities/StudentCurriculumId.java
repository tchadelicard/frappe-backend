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
public class StudentCurriculumId implements Serializable {
    private static final long serialVersionUID = -3858353867114103314L;
    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "curriculum_id", nullable = false)
    private Long curriculumId;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        StudentCurriculumId entity = (StudentCurriculumId) o;
        return Objects.equals(this.studentId, entity.studentId) &&
                Objects.equals(this.year, entity.year) &&
                Objects.equals(this.curriculumId, entity.curriculumId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, year, curriculumId);
    }

}