package fr.imt_atlantique.frappe.dtos;

import lombok.Data;

@Data
public class MinimalUserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private Long campusId;
}
