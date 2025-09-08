package com.example.clinic_skin_be.controller.medical;

import com.example.clinic_skin_be.model.medical.lab_test.LabTest;
import com.example.clinic_skin_be.model.medical.medication.Medication;
import com.example.clinic_skin_be.model.medical.procedure.Procedure;
import com.example.clinic_skin_be.model.medical.treatment_plan.StepType;
import com.example.clinic_skin_be.service.medical.treatment.TreatmentItemService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/treatment-items")
public class TreatmentItemRestController {

    private final TreatmentItemService treatmentItemService;

    // =================== Lab Tests ===================
    @GetMapping("/lab-tests")
    public List<LabTest> getAllLabTests() {
        return treatmentItemService.listlabTests();
    }

    @GetMapping("/lab-tests/{id}")
    public ResponseEntity<LabTest> getLabTest(@PathVariable Long id) {
        LabTest labTest = treatmentItemService.getLabTestById(id);
        return labTest != null ? ResponseEntity.ok(labTest) : ResponseEntity.notFound().build();
    }

    @PostMapping("/lab-tests")
    public LabTest createLabTest(@RequestBody LabTest labTest) {
        return treatmentItemService.saveLabTest(labTest);
    }

    @PutMapping("/lab-tests/{id}")
    public ResponseEntity<LabTest> updateLabTest(@PathVariable Long id, @RequestBody LabTest labTest) {
        LabTest existing = treatmentItemService.getLabTestById(id);
        if (existing == null) return ResponseEntity.notFound().build();
        labTest.setId(id);
        return ResponseEntity.ok(treatmentItemService.saveLabTest(labTest));
    }

    // =================== Medications ===================
    @GetMapping("/medications")
    public List<Medication> getAllMedications() {
        return treatmentItemService.listMedications();
    }

    @GetMapping("/medications/{id}")
    public ResponseEntity<Medication> getMedication(@PathVariable Long id) {
        Medication medication = treatmentItemService.getMedicationById(id);
        return medication != null ? ResponseEntity.ok(medication) : ResponseEntity.notFound().build();
    }

    @PostMapping("/medications")
    public Medication createMedication(@RequestBody Medication medication) {
        return treatmentItemService.saveMedication(medication);
    }

    @PutMapping("/medications/{id}")
    public ResponseEntity<Medication> updateMedication(@PathVariable Long id, @RequestBody Medication medication) {
        Medication existing = treatmentItemService.getMedicationById(id);
        if (existing == null) return ResponseEntity.notFound().build();
        medication.setId(id);
        return ResponseEntity.ok(treatmentItemService.saveMedication(medication));
    }

    // =================== Procedures ===================
    @GetMapping("/procedures")
    public List<Procedure> getAllProcedures() {
        return treatmentItemService.listProcedures();
    }

    @GetMapping("/procedures/{id}")
    public ResponseEntity<Procedure> getProcedure(@PathVariable Long id) {
        Procedure procedure = treatmentItemService.getProcedureById(id);
        return procedure != null ? ResponseEntity.ok(procedure) : ResponseEntity.notFound().build();
    }

    @PostMapping("/procedures")
    public Procedure createProcedure(@RequestBody Procedure procedure) {
        return treatmentItemService.saveProcedure(procedure);
    }

    @PutMapping("/procedures/{id}")
    public ResponseEntity<Procedure> updateProcedure(@PathVariable Long id, @RequestBody Procedure procedure) {
        Procedure existing = treatmentItemService.getProcedureById(id);
        if (existing == null) return ResponseEntity.notFound().build();
        procedure.setId(id);
        return ResponseEntity.ok(treatmentItemService.saveProcedure(procedure));
    }

    // =================== Step Types ===================
    @GetMapping("/step-types")
    public List<StepType> getAllStepTypes() {
        return treatmentItemService.listStepTypes();
    }

    @GetMapping("/step-types/{id}")
    public ResponseEntity<StepType> getStepType(@PathVariable Long id) {
        StepType stepType = treatmentItemService.getStepTypeById(id);
        return stepType != null ? ResponseEntity.ok(stepType) : ResponseEntity.notFound().build();
    }

    @PostMapping("/step-types")
    public StepType createStepType(@RequestBody StepType stepType) {
        return treatmentItemService.saveStepType(stepType);
    }

    @PutMapping("/step-types/{id}")
    public ResponseEntity<StepType> updateStepType(@PathVariable Long id, @RequestBody StepType stepType) {
        StepType existing = treatmentItemService.getStepTypeById(id);
        if (existing == null) return ResponseEntity.notFound().build();
        stepType.setTypeId(id);
        return ResponseEntity.ok(treatmentItemService.saveStepType(stepType));
    }

}
