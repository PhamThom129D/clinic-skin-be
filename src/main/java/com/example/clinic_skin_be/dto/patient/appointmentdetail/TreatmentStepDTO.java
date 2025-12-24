package com.example.clinic_skin_be.dto.patient.appointmentdetail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreatmentStepDTO {
    private Integer stepNumber;
    private String stepTypeName;
    private String stepDescription;
    private String notes;
    private String results;
}
