package fr.imt_atlantique.frappe.dtos;

import fr.imt_atlantique.frappe.entities.Student;
import fr.imt_atlantique.frappe.entities.Supervisor;
import lombok.Data;

import java.time.LocalDateTime;

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