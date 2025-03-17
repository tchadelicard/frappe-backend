package fr.imt_atlantique.frappe.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserDTO extends MinimalUserDTO {
    private String username;
    private String email;
    private String phoneNumber;
    private Long campusId;
}
