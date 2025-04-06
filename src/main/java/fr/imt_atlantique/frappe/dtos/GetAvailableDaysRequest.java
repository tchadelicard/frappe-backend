package fr.imt_atlantique.frappe.dtos;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetAvailableDaysRequest {
    String username;
    String password;
    String duration;
    LocalDate startDate;
    LocalDate endDate;
}
