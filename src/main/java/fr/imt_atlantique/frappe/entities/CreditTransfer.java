package fr.imt_atlantique.frappe.entities;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

}