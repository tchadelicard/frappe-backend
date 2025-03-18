package fr.imt_atlantique.frappe.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateUserRequest {
    private String username;

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Password must contain at least 8 characters, one digit, one lowercase letter, one uppercase letter and one special character")
    private String password;

    @Email(regexp = "[a-z0-9+_.-]+@imt-atlantique\\.(?:fr|net)$", flags = Pattern.Flag.CASE_INSENSITIVE, message = "Email must be an IMT Atlantique email")
    private String email;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    @Min(value = 1, message = "Campus ID must be greater than 0")
    private Long campusId;
}
