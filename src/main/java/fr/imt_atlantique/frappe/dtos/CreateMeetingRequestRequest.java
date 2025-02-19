package fr.imt_atlantique.frappe.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateMeetingRequestRequest {
    @NotBlank
    private LocalDateTime startDate;
    @NotBlank
    private LocalDateTime endDate;
    @NotBlank
    private String theme;
    @NotBlank
    private String location;
    @NotBlank
    private String requestDescription;
    @NotBlank
    private Long studentId;
    @NotBlank
    private Long supervisorId;
}