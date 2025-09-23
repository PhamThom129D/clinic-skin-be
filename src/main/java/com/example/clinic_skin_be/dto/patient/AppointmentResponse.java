package com.example.clinic_skin_be.dto.patient;

import com.example.clinic_skin_be.model.manage_enum.ConsultationStatus;
import lombok.Data;

@Data
public class AppointmentResponse {
    private Long id;
    private String appointmentDate; // yyyy-MM-dd
    private String appointmentTime; // HH:mm
    private String note;
    private ConsultationStatus status;
    private PatientResponse patient;
}
