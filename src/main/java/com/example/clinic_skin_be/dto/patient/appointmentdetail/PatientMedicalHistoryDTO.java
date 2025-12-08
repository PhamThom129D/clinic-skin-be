package com.example.clinic_skin_be.dto.patient.appointmentdetail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientMedicalHistoryDTO implements Serializable {

    // Hidden
    private Long recordId;
    private Long sessionId;

    // Hiển thị
    private String symptoms;
    private String diagnosis;
    private String doctorFullName;
    private String sessionDate; // YYYY-MM-DD HH:MM
}

