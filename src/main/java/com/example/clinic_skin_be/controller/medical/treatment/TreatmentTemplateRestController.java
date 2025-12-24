package com.example.clinic_skin_be.controller.medical.treatment;

import com.example.clinic_skin_be.dto.medical.treatment_template.TreatmentStepTemplateDTO;
import com.example.clinic_skin_be.dto.medical.treatment_template.TreatmentTemplateDTO;
import com.example.clinic_skin_be.model.medical.treatment_plan.StepType;
import com.example.clinic_skin_be.repository.medical.treatment_plan.IStepTypeRepository;
import com.example.clinic_skin_be.service.medical.treatment_template.TreatmentTemplateService;
import lombok.AllArgsConstructor;
import okhttp3.Request;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/treatment-templates")
@AllArgsConstructor
public class TreatmentTemplateRestController {

    private final TreatmentTemplateService treatmentTemplateService;
    private final IStepTypeRepository stepTypeRepository;

    // ------------------- TEMPLATE -------------------

    @GetMapping
    public List<TreatmentTemplateDTO> getAllTemplates() {
        return treatmentTemplateService.listTreatmentTemplates();
    }

    @GetMapping("/{id}")
    public TreatmentTemplateDTO getTemplate(@PathVariable Long id) {
        return treatmentTemplateService.getTreatmentTemplateById(id);
    }

    @PostMapping
    public TreatmentTemplateDTO createTemplate(@RequestBody TreatmentTemplateDTO dto) {
        return treatmentTemplateService.saveTreatmentTemplate(dto);
    }
    @PostMapping("/by-diagnose")
    public ResponseEntity<TreatmentTemplateDTO> getTreatmentTemplateByDiagnoseName(@RequestBody Map<String, String> request) {
        String diagnoseName = request.get("diagnoseName");
        TreatmentTemplateDTO dto = treatmentTemplateService.getTreatmentTemplateByDiagnoseName(diagnoseName);

        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }



    @PutMapping("/{id}")
    public TreatmentTemplateDTO updateTemplate(@PathVariable Long id, @RequestBody TreatmentTemplateDTO dto) {
        dto.setId(id);
        return treatmentTemplateService.saveTreatmentTemplate(dto);
    }

    @DeleteMapping("/{id}")
    public void deleteTemplate(@PathVariable Long id) {
        treatmentTemplateService.deleteTreatmentTemplate(id);
    }

    // ------------------- STEP -------------------

    @GetMapping("/steps")
    public List<TreatmentStepTemplateDTO> getAllSteps() {
        return treatmentTemplateService.listTreatmentStepTemplates();
    }

    @GetMapping("/steps/{id}")
    public TreatmentStepTemplateDTO getStep(@PathVariable Long id) {
        return treatmentTemplateService.getTreatmentStepTemplateById(id);
    }

    @PostMapping("/steps")
    public TreatmentStepTemplateDTO createStep(@RequestBody TreatmentStepTemplateDTO dto) {
        StepType stepType = stepTypeRepository.findById(dto.getStepTypeId())
                .orElseThrow(() -> new RuntimeException("StepType not found"));

        return treatmentTemplateService.saveTreatmentStepTemplate(dto, stepType);
    }

    @PutMapping("/steps/{id}")
    public TreatmentStepTemplateDTO updateStep(@PathVariable Long id, @RequestBody TreatmentStepTemplateDTO dto) {
        StepType stepType = stepTypeRepository.findById(dto.getStepTypeId())
                .orElseThrow(() -> new RuntimeException("StepType not found"));

        dto.setId(id); // đảm bảo DTO có ID
        return treatmentTemplateService.saveTreatmentStepTemplate(dto, stepType);
    }

    @DeleteMapping("/steps/{id}")
    public void deleteStep(@PathVariable Long id) {
        treatmentTemplateService.deleteTreatmentStepTemplate(id);
    }
}
