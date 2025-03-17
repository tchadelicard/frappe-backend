package fr.imt_atlantique.frappe.dtos;

import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateUserRequest extends CreateUpdateUserRequest {
    private String phoneNumber;

    @Min(value = 1, message = "Campus ID must be greater than 0")
    private Long campusId;
}
