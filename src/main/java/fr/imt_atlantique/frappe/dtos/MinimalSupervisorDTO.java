package fr.imt_atlantique.frappe.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MinimalSupervisorDTO extends MinimalUserDTO {
    private String meetingUrl;
}
