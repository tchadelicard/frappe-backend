package fr.imt_atlantique.frappe.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ActionDTO {
    private Long id;
    private String notes;
    private String actionPlan;
}
