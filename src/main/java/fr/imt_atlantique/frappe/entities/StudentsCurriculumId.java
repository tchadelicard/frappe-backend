package fr.imt_atlantique.frappe.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class StudentsCurriculumId implements Serializable {
    private static final long serialVersionUID = -4242160448306787509L;
    @Column(name = "student_id", nullable = false)
    private Integer studentId;

    @Column(name = "curriculum_id", nullable = false)
    private Integer curriculumId;

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getCurriculumId() {
        return curriculumId;
    }

    public void setCurriculumId(Integer curriculumId) {
        this.curriculumId = curriculumId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        StudentsCurriculumId entity = (StudentsCurriculumId) o;
        return Objects.equals(this.studentId, entity.studentId) &&
                Objects.equals(this.curriculumId, entity.curriculumId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, curriculumId);
    }

}