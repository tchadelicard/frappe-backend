package fr.imt_atlantique.frappe.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class GetAvailableDaysRequest {
    String username;
    String password;
    String duration;
    LocalDate startDate;
    LocalDate endDate;
}
