package fr.imt_atlantique.frappe.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VerifyResponse {
    private boolean success;
    private String message;
}
