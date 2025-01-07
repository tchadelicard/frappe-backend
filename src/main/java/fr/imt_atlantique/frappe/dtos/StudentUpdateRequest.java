package fr.imt_atlantique.frappe.dtos;

import lombok.Data;

@Data
public class StudentUpdateRequest extends UserUpdateRequest{
    private String gender;
    private String nationality;
    private Long creditTransferId;
}
