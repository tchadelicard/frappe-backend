package fr.imt_atlantique.frappe.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "actions")
public class Action {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "actions_id_gen")
    @SequenceGenerator(name = "actions_id_gen", sequenceName = "actions_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "notes", nullable = false, length = Integer.MAX_VALUE)
    private String notes;

    @Column(name = "action_plan", nullable = false, length = Integer.MAX_VALUE)
    private String actionPlan;
}