package com.example.clinic_skin_be.service.medical.treatment_plan;

import com.example.clinic_skin_be.dto.medical.treatment_template.TreatmentStepTemplateDTO;
import com.example.clinic_skin_be.dto.medical.treatment_template.TreatmentTemplateDTO;
import com.example.clinic_skin_be.mapper.TreatmentPlanMapper;
import com.example.clinic_skin_be.model.medical.treatment_plan.StepType;
import com.example.clinic_skin_be.model.medical.treatment_plan.TreatmentPlan;
import com.example.clinic_skin_be.model.medical.treatment_plan.TreatmentStep;
import com.example.clinic_skin_be.repository.medical.treatment_plan.ITreatmentPlanRepository;
import com.example.clinic_skin_be.repository.medical.treatment_plan.ITreatmentStepRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TreatmentPlanService {

    private final ITreatmentPlanRepository planRepo;
    private final ITreatmentStepRepository stepRepo;
    private final TreatmentPlanMapper mapper;

    // ------------------- TreatmentPlan -------------------

    public List<TreatmentTemplateDTO> listAllPlans() {
        return mapper.toDTOList(planRepo.findAll());
    }

    public TreatmentTemplateDTO getPlanById(Long id) {
        return planRepo.findById(id).map(mapper::toDTO).orElse(null);
    }

    public TreatmentTemplateDTO savePlan(TreatmentTemplateDTO dto) {
        TreatmentPlan plan = mapper.fromDTO(dto);
        TreatmentPlan saved = planRepo.save(plan);
        return mapper.toDTO(saved);
    }

    public void deletePlan(Long id) {
        planRepo.deleteById(id);
    }

    // ------------------- TreatmentStep -------------------

    public List<TreatmentStepTemplateDTO> listStepsByPlan(Long planId) {
        return mapper.toStepDTOList(stepRepo.findAllByTreatmentPlanId(planId));
    }

    public TreatmentStepTemplateDTO saveStep(TreatmentStepTemplateDTO dto, StepType stepType) {
        TreatmentPlan plan = new TreatmentPlan();
        plan.setId(dto.getTreatmentId());

        TreatmentStep step = mapper.fromDTO(dto, plan, stepType);
        TreatmentStep saved = stepRepo.save(step);
        return mapper.toStepDTO(saved);
    }
    public TreatmentStepTemplateDTO getStepById(Long id) {
        return stepRepo.findById(id)
                .map(mapper::toStepDTO)
                .orElse(null);
    }


    public void deleteStep(Long id) {
        stepRepo.deleteById(id);
    }
}
