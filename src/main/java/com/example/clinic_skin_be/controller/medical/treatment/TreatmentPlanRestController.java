package com.example.clinic_skin_be.controller.medical.treatment;

import com.example.clinic_skin_be.dto.medical.treatment_template.TreatmentStepTemplateDTO;
import com.example.clinic_skin_be.dto.medical.treatment_template.TreatmentTemplateDTO;
import com.example.clinic_skin_be.model.medical.treatment_plan.StepType;
import com.example.clinic_skin_be.repository.medical.treatment_plan.IStepTypeRepository;
import com.example.clinic_skin_be.service.medical.treatment_plan.TreatmentPlanService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/treatment-plans")
@AllArgsConstructor
public class TreatmentPlanRestController {

    private final TreatmentPlanService planService;
    private final IStepTypeRepository stepTypeRepository;

    // ------------------- PLAN -------------------

    @GetMapping
    public List<TreatmentTemplateDTO> getAllPlans() {
        return planService.listAllPlans();
    }

    @GetMapping("/{id}")
    public TreatmentTemplateDTO getPlan(@PathVariable Long id) {
        return planService.getPlanById(id);
    }

    @PostMapping
    public TreatmentTemplateDTO createPlan(@RequestBody TreatmentTemplateDTO dto) {
        return planService.savePlan(dto);
    }

    @PutMapping("/{id}")
    public TreatmentTemplateDTO updatePlan(@PathVariable Long id, @RequestBody TreatmentTemplateDTO dto) {
        dto.setId(id); // đảm bảo DTO có ID
        return planService.savePlan(dto);
    }

    @DeleteMapping("/{id}")
    public void deletePlan(@PathVariable Long id) {
        planService.deletePlan(id);
    }

    // ------------------- STEP -------------------

    @GetMapping("/steps")
    public List<TreatmentStepTemplateDTO> getAllSteps() {
        return planService.listStepsByPlan(null);
    }

    @GetMapping("/steps/{id}")
    public TreatmentStepTemplateDTO getStep(@PathVariable Long id) {
        return planService.getStepById(id);
    }

    @PostMapping("/steps")
    public TreatmentStepTemplateDTO createStep(@RequestBody TreatmentStepTemplateDTO dto) {
        StepType stepType = stepTypeRepository.findById(dto.getStepTypeId())
                .orElseThrow(() -> new RuntimeException("StepType not found"));

        return planService.saveStep(dto, stepType);
    }

    @PutMapping("/steps/{id}")
    public TreatmentStepTemplateDTO updateStep(@PathVariable Long id, @RequestBody TreatmentStepTemplateDTO dto) {
        StepType stepType = stepTypeRepository.findById(dto.getStepTypeId())
                .orElseThrow(() -> new RuntimeException("StepType not found"));

        dto.setId(id);
        return planService.saveStep(dto, stepType);
    }

    @DeleteMapping("/steps/{id}")
    public void deleteStep(@PathVariable Long id) {
        planService.deleteStep(id);
    }
}
