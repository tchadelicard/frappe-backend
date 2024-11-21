package fr.imt_atlantique.frappe.entities;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
public class StudentCurriculumId implements Serializable {
    private Long studentId;
    private Long curriculumId;
    private Integer year;
}
