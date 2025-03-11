package fr.imt_atlantique.frappe.dtos;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MeetingRequestDTO {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String theme;
    private String location;
    private String requestDescription;
    private String status;
    private Long studentId;
    private Long supervisorId;
}