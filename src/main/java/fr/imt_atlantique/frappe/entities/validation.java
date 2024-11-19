
package fr.imt_atlantique.frappe.entities;

import jakarta.persistence.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "validation")
public class validation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "validation_id_gen")
    private int id;
    
    private Instant creation;
    private Instant expire;
    private Instant activation;
    private String token;
    @OneToOne(cascade = CascadeType.ALL)
    private User user;

}
