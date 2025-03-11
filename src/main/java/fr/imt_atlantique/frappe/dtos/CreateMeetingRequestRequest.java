package fr.imt_atlantique.frappe.dtos;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateMeetingRequestRequest {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    @NotBlank
    private String theme;
    @NotBlank
    private String location;
    @NotBlank
    private String requestDescription;
    private Long studentId;
    private Long supervisorId;
}