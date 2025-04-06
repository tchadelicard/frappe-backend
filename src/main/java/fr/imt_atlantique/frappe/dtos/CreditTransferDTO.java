package fr.imt_atlantique.frappe.dtos;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreditTransferDTO {
    private Long id;
    private String university;
    private String country;
    private LocalDate startDate;
    private LocalDate endDate;
}
