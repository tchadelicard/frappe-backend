package fr.imt_atlantique.frappe.dtos;

import lombok.Data;

@Data
public class StudentDTO extends UserDTO {
    private String gender;
    private String nationality;
    private Long creditTransferId;
}
