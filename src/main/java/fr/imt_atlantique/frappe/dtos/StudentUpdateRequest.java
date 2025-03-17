package fr.imt_atlantique.frappe.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class StudentUpdateRequest extends UserUpdateRequest {
    private String gender;
    private String nationality;
    private Long creditTransferId;
}
