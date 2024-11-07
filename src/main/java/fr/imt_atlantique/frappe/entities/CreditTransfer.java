package fr.imt_atlantique.frappe.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "credit_transfers")
public class CreditTransfer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "credit_transfers_id_gen")
    @SequenceGenerator(name = "credit_transfers_id_gen", sequenceName = "credit_transfers_credit_transfer_id_seq", allocationSize = 1)
    @Column(name = "credit_transfer_id", nullable = false)
    private Long id;

    @Column(name = "university", nullable = false)
    private String university;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @OneToMany(mappedBy = "creditTransfer")
    private Set<Student> students = new LinkedHashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

}