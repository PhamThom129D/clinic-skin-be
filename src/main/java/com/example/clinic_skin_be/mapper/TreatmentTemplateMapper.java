package com.example.clinic_skin_be.mapper;

import com.example.clinic_skin_be.dto.medical.treatment_template.PrescriptionDTO;
import com.example.clinic_skin_be.dto.medical.treatment_template.TreatmentStepTemplateDTO;
import com.example.clinic_skin_be.dto.medical.treatment_template.TreatmentTemplateDTO;
import com.example.clinic_skin_be.model.medical.lab_test.LabTest;
import com.example.clinic_skin_be.model.medical.medication.Prescription;
import com.example.clinic_skin_be.model.medical.procedure.Procedure;
import com.example.clinic_skin_be.model.medical.treatment_plan.StepType;
import com.example.clinic_skin_be.model.medical.treatment_template.TreatmentStepTemplate;
import com.example.clinic_skin_be.model.medical.treatment_template.TreatmentTemplate;
import com.example.clinic_skin_be.repository.medical.lab_test.ILabTestRepository;
import com.example.clinic_skin_be.repository.medical.medication.IMedicationRepository;
import com.example.clinic_skin_be.repository.medical.medication.IPrescriptionRepository;
import com.example.clinic_skin_be.repository.medical.procedure.IProcedureRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class TreatmentTemplateMapper {
    private final ILabTestRepository labTestRepository;
    private final IProcedureRepository procedureRepository;
    private final IPrescriptionRepository prescriptionRepository;
    private final PrescriptionMapper prescriptionMapper;


    // ------------------- ENTITY -> DTO -------------------

    public TreatmentStepTemplateDTO toDTO(TreatmentStepTemplate step) {
        if (step == null) return null;

        TreatmentStepTemplateDTO dto = new TreatmentStepTemplateDTO();
        dto.setId(step.getId());
        dto.setStepNumber(step.getStepNumber());
        dto.setStepTypeId(step.getStepType().getTypeId());
        dto.setStepTypeName(step.getStepType().getTypeName());
        dto.setStepDesc(step.getStepType().getDescription());
        dto.setItemId(step.getItemId());
        dto.setNotes(step.getNotes());
        dto.setTreatmentId(step.getTemplate() != null ? step.getTemplate().getId() : null);
        dto.setTreatmentName(step.getTemplate() != null ? step.getTemplate().getName() : null);


        // Tùy theo typeId, load itemDetails
        switch (step.getStepType().getTypeName().toLowerCase()) {
            case "medication":
                Prescription prescription = prescriptionRepository.findById(step.getItemId()).orElse(null);
                if (prescription != null) {
                    PrescriptionDTO prescriptionDTO = prescriptionMapper.toDTO(prescription);
                    dto.setItemDetails(prescriptionDTO);
                } else {
                    dto.setItemDetails(null);
                }
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


    public TreatmentTemplateDTO toDTO(TreatmentTemplate template) {
        if (template == null) return null;
        List<TreatmentStepTemplateDTO> stepsDTO = template.getSteps().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return new TreatmentTemplateDTO(
                template.getId(),
                template.getName(),
                template.getDisease_name(),
                template.getDescription(),
                stepsDTO
        );
    }

    // ------------------- DTO -> ENTITY -------------------

    public TreatmentStepTemplate fromDTO(TreatmentStepTemplateDTO dto, TreatmentTemplate parentTemplate, StepType stepType) {
        TreatmentStepTemplate step = new TreatmentStepTemplate();
        step.setId(dto.getId());
        step.setStepNumber(dto.getStepNumber());
        step.setStepType(stepType);
        step.setItemId(dto.getItemId());
        step.setNotes(dto.getNotes());
        step.setTemplate(parentTemplate);
        return step;
    }

    public TreatmentTemplate fromDTO(TreatmentTemplateDTO dto) {
        TreatmentTemplate template = new TreatmentTemplate();
        template.setId(dto.getId());
        template.setName(dto.getName());
        template.setDescription(dto.getDescription());
        return template;
    }

    public List<TreatmentTemplateDTO> toDTOList(List<TreatmentTemplate> templates) {
        return templates.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<TreatmentStepTemplateDTO> toStepDTOList(List<TreatmentStepTemplate> steps) {
        return steps.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
