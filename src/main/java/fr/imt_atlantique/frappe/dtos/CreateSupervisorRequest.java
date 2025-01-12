package fr.imt_atlantique.frappe.dtos;

import lombok.Data;

@Data
public class CreateSupervisorRequest {
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String meetingUrl;
    private String caldavUsername;
    private String caldavPassword;
}
