package fr.imt_atlantique.frappe.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Setter
@Getter
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