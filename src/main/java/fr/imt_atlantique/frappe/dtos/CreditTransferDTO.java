package fr.imt_atlantique.frappe.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class CreditTransferDTO {
    private Long id;
    private String university;
    private String country;
    private LocalDate startDate;
    private LocalDate endDate;
}
