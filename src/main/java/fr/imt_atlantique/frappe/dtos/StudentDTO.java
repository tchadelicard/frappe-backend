package fr.imt_atlantique.frappe.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class StudentDTO extends UserDTO {
    private String gender;
    private String nationality;
    private Long creditTransferId;
}
