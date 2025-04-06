package fr.imt_atlantique.frappe.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreateUserRequest {
    @NotBlank(message = "Username is mandatory")
    private String username;

    @NotBlank(message = "Password is mandatory")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Password must contain at least 8 characters, one digit, one lowercase letter, one uppercase letter and one special character")
    private String password;

    @NotBlank(message = "Email is mandatory")
    @Email(regexp = "[a-z0-9+_.-]+@imt-atlantique\\.(?:fr|net)$", flags = Pattern.Flag.CASE_INSENSITIVE, message = "Email must be an IMT Atlantique email")
    private String email;

    @NotBlank(message = "First name is mandatory")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    private String lastName;
}
