package fr.imt_atlantique.frappe.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreateSupervisorRequest extends CreateUserRequest {
    @NotNull(message = "Campus ID is mandatory")
    private Long campusId;
}
