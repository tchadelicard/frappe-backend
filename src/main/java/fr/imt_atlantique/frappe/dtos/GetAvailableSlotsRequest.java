package fr.imt_atlantique.frappe.dtos;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetAvailableSlotsRequest {
    String username;
    String password;
    LocalDate date;
    String duration;
}
