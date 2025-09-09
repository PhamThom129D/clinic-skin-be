package com.example.clinic_skin_be.service.medical.treatment_template;

import com.example.clinic_skin_be.dto.medical.treatment_template.TreatmentStepTemplateDTO;
import com.example.clinic_skin_be.dto.medical.treatment_template.TreatmentTemplateDTO;
import com.example.clinic_skin_be.mapper.TreatmentTemplateMapper;
import com.example.clinic_skin_be.model.medical.treatment_plan.StepType;
import com.example.clinic_skin_be.model.medical.treatment_template.TreatmentStepTemplate;
import com.example.clinic_skin_be.model.medical.treatment_template.TreatmentTemplate;
import com.example.clinic_skin_be.repository.medical.treatmnet_template.ITreatmentStepTemplateRepository;
import com.example.clinic_skin_be.repository.medical.treatmnet_template.ITreatmentTemplateRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TreatmentTemplateService {

    private final ITreatmentTemplateRepository treatmentTemplateRepository;
    private final ITreatmentStepTemplateRepository treatmentStepTemplateRepository;
    private final TreatmentTemplateMapper mapper;

    // ------------------- CRUD -------------------

    public List<TreatmentTemplateDTO> listTreatmentTemplates() {
        return mapper.toDTOList(treatmentTemplateRepository.findAll());
    }

    public List<TreatmentStepTemplateDTO> listTreatmentStepTemplates() {
        return mapper.toStepDTOList(treatmentStepTemplateRepository.findAll());
    }

    public TreatmentTemplateDTO getTreatmentTemplateById(Long id) {
        return treatmentTemplateRepository.findById(id).map(mapper::toDTO).orElse(null);
    }
    public TreatmentTemplateDTO getTreatmentTemplateByDiagnoseName(String diagnoseName) {
        return treatmentTemplateRepository.findTreatmentTemplateByDiseaseName(diagnoseName)
                .map(mapper::toDTO)
                .orElse(null);
    }


    public TreatmentStepTemplateDTO getTreatmentStepTemplateById(Long id) {
        return treatmentStepTemplateRepository.findById(id).map(mapper::toDTO).orElse(null);
    }

    public TreatmentTemplateDTO saveTreatmentTemplate(TreatmentTemplateDTO dto) {
        TreatmentTemplate template = mapper.fromDTO(dto);
        TreatmentTemplate saved = treatmentTemplateRepository.save(template);
        return mapper.toDTO(saved);
    }

    public TreatmentStepTemplateDTO saveTreatmentStepTemplate(TreatmentStepTemplateDTO dto, StepType stepType) {
        TreatmentTemplate parentTemplate = new TreatmentTemplate();
        parentTemplate.setId(dto.getTreatmentId());

        TreatmentStepTemplate step = mapper.fromDTO(dto, parentTemplate, stepType);
        TreatmentStepTemplate saved = treatmentStepTemplateRepository.save(step);
        return mapper.toDTO(saved);
    }


    public void deleteTreatmentTemplate(Long id) {
        treatmentTemplateRepository.deleteById(id);
    }

    public void deleteTreatmentStepTemplate(Long id) {
        treatmentStepTemplateRepository.deleteById(id);
    }
}
