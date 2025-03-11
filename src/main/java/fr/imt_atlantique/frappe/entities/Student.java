package fr.imt_atlantique.frappe.entities;

import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "students")
@PrimaryKeyJoinColumn(name = "student_id")
public class Student extends User {
    @Column(name = "gender")
    private String gender;

    @Column(name = "nationality")
    private String nationality;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_transfer_id")
    private CreditTransfer creditTransfer;

    @OneToMany(mappedBy = "student")
    private Set<MeetingRequest> meetingRequests = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "students")
    private Set<Curriculum> curriculums = new LinkedHashSet<>();

    @OneToMany(mappedBy = "student")
    private Set<StudentSpecialtyPerYear> studentsSpecialtiesPerYears = new LinkedHashSet<>();

    @Override
    public Set<GrantedAuthority> getAuthorities() {
        return Set.of(new SimpleGrantedAuthority("ROLE_STUDENT"));
    }
}