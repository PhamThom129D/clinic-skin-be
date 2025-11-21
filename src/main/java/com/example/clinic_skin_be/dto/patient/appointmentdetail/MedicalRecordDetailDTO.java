package com.example.clinic_skin_be.dto.patient.appointmentdetail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordDetailDTO {
    private AppointmentSummaryDTO appointmentInfo;
    private DoctorSummaryDTO doctorInfo;
    private ClinicalDetailsDTO clinicalDetails;
    private TreatmentPlanSummaryDTO treatmentPlan;
    private List<TreatmentStepDTO> steps;
}
