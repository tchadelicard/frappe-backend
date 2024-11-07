package fr.imt_atlantique.frappe.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "campuses")
public class Campus {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "campuses_id_gen")
    @SequenceGenerator(name = "campuses_id_gen", sequenceName = "campuses_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}