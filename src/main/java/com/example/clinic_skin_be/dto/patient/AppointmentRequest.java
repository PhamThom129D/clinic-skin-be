package com.example.clinic_skin_be.dto.patient;

import lombok.Data;

@Data
public class AppointmentRequest {
    private String fullName;
    private String email;
    private String phoneNumber;
    private String passportNumber;
    private String occupation;
    private String address;
    private String gender; // MALE/FEMALE/OTHER
    private String dateOfBirth; // yyyy-MM-dd
    private String appointmentDate; // yyyy-MM-dd
    private String appointmentTime; // HH:mm
    private String note;
    private Long doctorId;
}
