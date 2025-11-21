package com.example.clinic_skin_be.dto.patient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentHistorySummaryDTO implements Serializable {

    // Hidden
    private Long appointmentId;
    private Long recordId;

    // Hiển thị
    private String status;
    private String appointmentDateTime;  // YYYY-MM-DD HH:MM
    private String appointmentNote;
    private String doctorName;
}

