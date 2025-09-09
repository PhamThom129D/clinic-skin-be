package com.example.clinic_skin_be.mapper;

import com.example.clinic_skin_be.dto.medical.treatment_template.TreatmentStepTemplateDTO;
import com.example.clinic_skin_be.dto.medical.treatment_template.TreatmentTemplateDTO;
import com.example.clinic_skin_be.model.medical.lab_test.LabTest;
import com.example.clinic_skin_be.model.medical.medication.Prescription;
import com.example.clinic_skin_be.model.medical.procedure.Procedure;
import com.example.clinic_skin_be.model.medical.treatment_plan.StepType;
import com.example.clinic_skin_be.model.medical.treatment_plan.TreatmentPlan;
import com.example.clinic_skin_be.model.medical.treatment_plan.TreatmentStep;
import com.example.clinic_skin_be.repository.medical.lab_test.ILabTestRepository;
import com.example.clinic_skin_be.repository.medical.medication.IPrescriptionRepository;
import com.example.clinic_skin_be.repository.medical.procedure.IProcedureRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class TreatmentPlanMapper {

    private final IPrescriptionRepository prescriptionRepository;
    private final ILabTestRepository labTestRepository;
    private final IProcedureRepository procedureRepository;
    private final PrescriptionMapper prescriptionMapper;

    // ------------------- ENTITY -> DTO -------------------

    public TreatmentStepTemplateDTO toStepDTO(TreatmentStep step) {
        if (step == null) return null;

        TreatmentStepTemplateDTO dto = new TreatmentStepTemplateDTO();
        dto.setId(step.getId());
        dto.setStepNumber(step.getStepNumber());
        dto.setStepTypeId(step.getStepType().getTypeId());
        dto.setStepTypeName(step.getStepType().getTypeName());
        dto.setStepDesc(step.getStepType().getDescription());
        dto.setItemId(step.getItemId());
        dto.setNotes(step.getNotes());
        dto.setTreatmentId(step.getTreatmentPlan() != null ? step.getTreatmentPlan().getId() : null);
        dto.setTreatmentName(step.getTreatmentPlan() != null ? step.getTreatmentPlan().getTreatmentName() : null);
        dto.setResults(step.getResults());

        // Load itemDetails theo loại
        switch (step.getStepType().getTypeName().toLowerCase()) {
            case "medication":
                Prescription prescription = prescriptionRepository.findById(step.getItemId()).orElse(null);
                dto.setItemDetails(prescription != null ? prescriptionMapper.toDTO(prescription) : null);
                break;
            case "labtest":
                LabTest lab = labTestRepository.findById(step.getItemId()).orElse(null);
                dto.setItemDetails(lab);
                break;
            case "procedure":
                Procedure proc = procedureRepository.findById(step.getItemId()).orElse(null);
                dto.setItemDetails(proc);
                break;
            default:
                dto.setItemDetails(null);
        }

        return dto;
    }

    public List<TreatmentStepTemplateDTO> toStepDTOList(List<TreatmentStep> steps) {
        return steps.stream().map(this::toStepDTO).collect(Collectors.toList());
    }

    public TreatmentTemplateDTO toDTO(TreatmentPlan plan) {
        if (plan == null) return null;
        List<TreatmentStepTemplateDTO> stepsDTO = toStepDTOList(plan.getSteps());
        return new TreatmentTemplateDTO(
                plan.getId(),
                plan.getTreatmentName(),
                plan.getDisease_name(),
                null, // TreatmentPlan không có description, để null
                stepsDTO
        );
    }

    // ------------------- DTO -> ENTITY -------------------

    public TreatmentStep fromDTO(TreatmentStepTemplateDTO dto, TreatmentPlan plan, StepType stepType) {
        TreatmentStep step = new TreatmentStep();
        step.setId(dto.getId());
        step.setStepNumber(dto.getStepNumber());
        step.setStepType(stepType);
        step.setItemId(dto.getItemId() != null ? dto.getItemId() : 0);
        step.setNotes(dto.getNotes());
        step.setTreatmentPlan(plan);
        return step;
    }

    public TreatmentPlan fromDTO(TreatmentTemplateDTO dto) {
        TreatmentPlan plan = new TreatmentPlan();
        plan.setId(dto.getId());
        plan.setTreatmentName(dto.getDisease_name());
        plan.setTreatmentName(dto.getName());
        return plan;
    }

    public List<TreatmentTemplateDTO> toDTOList(List<TreatmentPlan> plans) {
        return plans.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
