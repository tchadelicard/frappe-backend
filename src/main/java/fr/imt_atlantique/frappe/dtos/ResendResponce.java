package fr.imt_atlantique.frappe.dtos;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ResendResponce {
    private boolean success;
    private String message;
}



