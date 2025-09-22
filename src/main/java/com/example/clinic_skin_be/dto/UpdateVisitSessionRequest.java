package com.example.clinic_skin_be.dto;

import com.example.clinic_skin_be.dto.medical.VisitSessionDTO;
import com.example.clinic_skin_be.dto.medical.treatment_template.PrescriptionDetailDTO;
import com.example.clinic_skin_be.dto.medical.treatment_template.TreatmentStepTemplateDTO;
import com.example.clinic_skin_be.model.medical.lab_test.LabTest;
import lombok.Data;

import java.util.List;

@Data
public class UpdateVisitSessionRequest {
    private VisitSessionDTO session;
    private List<TreatmentStepTemplateDTO> steps;
    private List<PrescriptionDetailDTO> prescriptions;
    private List<LabTest> labTests;
}

