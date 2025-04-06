package fr.imt_atlantique.frappe.dtos;

import java.time.OffsetDateTime;

import lombok.Data;

@Data
public class AvailabilitySlotDTO {
    private String duration;
    private OffsetDateTime start;
    private OffsetDateTime end;
    private boolean remote;
}
