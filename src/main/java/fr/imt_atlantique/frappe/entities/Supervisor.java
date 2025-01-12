package fr.imt_atlantique.frappe.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "supervisors")
@PrimaryKeyJoinColumn(name = "supervisor_id")
public class Supervisor extends User {
    @Column(name = "meeting_url")
    private String meetingUrl;

    @Column(name = "caldav_username")
    private String caldavUsername;

    @Column(name = "caldav_password")
    private String caldavPassword;

    @Column(name = "caldav_password_salt")
    private String caldavPasswordSalt;

    @Column(name = "caldav_password_iv")
    private String caldavPasswordIv;

    @OneToMany(mappedBy = "supervisor")
    private Set<MeetingRequest> meetingRequests = new LinkedHashSet<>();

    @Override
    public Set<GrantedAuthority> getAuthorities() {
        return Set.of(new SimpleGrantedAuthority("ROLE_SUPERVISOR"));
    }

}