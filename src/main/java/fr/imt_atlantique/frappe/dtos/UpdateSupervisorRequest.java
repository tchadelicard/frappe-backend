package fr.imt_atlantique.frappe.dtos;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateSupervisorRequest extends UpdateUserRequest {
    @URL
    private String meetingUrl;

    @Email(regexp = "[a-z0-9+_.-]+@imt-atlantique\\.(?:fr|net)$", flags = Pattern.Flag.CASE_INSENSITIVE, message = "Email must be an IMT Atlantique email")
    private String caldavUsername;

    private String caldavPassword;
}
