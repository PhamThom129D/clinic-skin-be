package com.example.clinic_skin_be.dto;

import com.example.clinic_skin_be.dto.medical.DoctorVisitSessionDTO;
import com.example.clinic_skin_be.dto.medical.VisitSessionDTO;
import com.example.clinic_skin_be.dto.medical.treatment_template.PrescriptionDetailDTO;
import com.example.clinic_skin_be.dto.medical.treatment_template.TreatmentStepTemplateDTO;
import lombok.Data;

import java.util.List;

@Data
public class UpdateVisitSessionRequest {
    private VisitSessionDTO session;
    private DoctorVisitSessionDTO doctorVisit;
    private List<TreatmentStepTemplateDTO> steps;
    private List<PrescriptionDetailDTO> prescriptions;
}

