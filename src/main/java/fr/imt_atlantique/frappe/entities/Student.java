package fr.imt_atlantique.frappe.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class Student {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id", nullable = false)
    private User user;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "nationality", nullable = false)
    private String nationality;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_transfer_id")
    private CreditTransfer creditTransfer;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public CreditTransfer getCreditTransfer() {
        return creditTransfer;
    }

    public void setCreditTransfer(CreditTransfer creditTransfer) {
        this.creditTransfer = creditTransfer;
    }

}