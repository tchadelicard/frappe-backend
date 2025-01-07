package fr.imt_atlantique.frappe.dtos;


import lombok.Data;

@Data
public class DailyAvailabilityRequest {
    private String supervisorid;
    private String date;
    private String duration;
}
