package com.example.clinic_skin_be.dto.patient.appointmentdetail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentSummaryDTO {
    private Long appointmentId;
    private String status; // COMPLETED, IN_PROGRESS, ...
    private String appointmentDateTime; // YYYY-MM-DD HH:MM
    private String note;
}
