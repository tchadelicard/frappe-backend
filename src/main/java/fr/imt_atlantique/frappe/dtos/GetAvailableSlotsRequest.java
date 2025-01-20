package fr.imt_atlantique.frappe.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class GetAvailableSlotsRequest {
    String username;
    String password;
    LocalDate date;
    String duration;
}
