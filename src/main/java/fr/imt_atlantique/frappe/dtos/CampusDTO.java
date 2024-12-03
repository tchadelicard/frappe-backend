package fr.imt_atlantique.frappe.dtos;

import fr.imt_atlantique.frappe.entities.Campus;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class CampusDTO {
    Long id;
    String name;
}