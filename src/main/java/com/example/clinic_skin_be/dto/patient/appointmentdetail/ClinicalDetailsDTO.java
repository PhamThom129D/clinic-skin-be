package com.example.clinic_skin_be.dto.patient.appointmentdetail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClinicalDetailsDTO {
    private String symptoms;
    private String diagnosis;
    private String clinicalNotes;
}
