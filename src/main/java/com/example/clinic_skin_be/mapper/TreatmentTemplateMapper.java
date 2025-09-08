package com.example.clinic_skin_be.mapper;

import com.example.clinic_skin_be.dto.medical.treatment_template.TreatmentStepTemplateDTO;
import com.example.clinic_skin_be.dto.medical.treatment_template.TreatmentTemplateDTO;
import com.example.clinic_skin_be.model.medical.treatment_plan.StepType;
import com.example.clinic_skin_be.model.medical.treatment_template.TreatmentStepTemplate;
import com.example.clinic_skin_be.model.medical.treatment_template.TreatmentTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TreatmentTemplateMapper {

    // ------------------- ENTITY -> DTO -------------------

    public TreatmentStepTemplateDTO toDTO(TreatmentStepTemplate step) {
        if (step == null) return null;
        return new TreatmentStepTemplateDTO(
                step.getId(),
                step.getStepNumber(),
                step.getStepType() != null ? step.getStepType().getTypeId() : null,
                step.getStepType() != null ? step.getStepType().getTypeName() : null,
                step.getStepType() != null ? step.getStepType().getDescription() : null,
                step.getItemId(),
                step.getNotes(),
                step.getTemplate() != null ? step.getTemplate().getId() : null,
                step.getTemplate() != null ? step.getTemplate().getName() : null
        );
    }

    public TreatmentTemplateDTO toDTO(TreatmentTemplate template) {
        if (template == null) return null;
        List<TreatmentStepTemplateDTO> stepsDTO = template.getSteps().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return new TreatmentTemplateDTO(
                template.getId(),
                template.getName(),
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
