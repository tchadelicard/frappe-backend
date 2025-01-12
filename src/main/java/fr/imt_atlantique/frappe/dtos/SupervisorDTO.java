package fr.imt_atlantique.frappe.dtos;

import lombok.Data;

@Data
public class SupervisorDTO extends UserDTO {
    private String meetingUrl;

}
