package fr.imt_atlantique.frappe.dtos;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class AvailabilitySlotDTO {
    private String duration;
    private OffsetDateTime start;
    private OffsetDateTime end;
    private boolean remote;
}
