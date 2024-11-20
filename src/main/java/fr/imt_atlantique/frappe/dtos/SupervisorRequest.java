package fr.imt_atlantique.frappe.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SupervisorRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}