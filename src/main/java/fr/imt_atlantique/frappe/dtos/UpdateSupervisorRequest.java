package fr.imt_atlantique.frappe.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class UpdateSupervisorRequest {
    @NotBlank
    private String username;

    @Email(regexp = "[a-z0-9+_.-]+@imt-atlantique\\.(?:fr|net)$", flags = Pattern.Flag.CASE_INSENSITIVE, message = "Email must be an IMT Atlantique email")
    private String email;

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Password must contain at least 8 characters, one digit, one lowercase letter, one uppercase letter and one special character")
    private String password;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private Long campusId;

    @URL
    private String meetingUrl;

    @Email(regexp = "[a-z0-9+_.-]+@imt-atlantique\\.(?:fr|net)$", flags = Pattern.Flag.CASE_INSENSITIVE, message = "Email must be an IMT Atlantique email")
    private String caldavUsername;

    private String caldavPassword;
}
