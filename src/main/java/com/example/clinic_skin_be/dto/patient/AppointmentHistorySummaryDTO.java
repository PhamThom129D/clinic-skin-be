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

    // Dữ liệu hiển thị
    private String status;
    private String appointmentDateTime; // YYYY-MM-DD HH:MM
    private String symptoms;
    private String doctorName;
}
