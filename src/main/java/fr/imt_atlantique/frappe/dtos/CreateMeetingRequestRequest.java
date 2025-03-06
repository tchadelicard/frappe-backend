package fr.imt_atlantique.frappe.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

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