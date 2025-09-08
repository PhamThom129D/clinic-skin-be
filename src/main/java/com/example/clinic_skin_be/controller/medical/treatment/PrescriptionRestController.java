package com.example.clinic_skin_be.controller.medical.treatment;

import com.example.clinic_skin_be.dto.medical.treatment_template.PrescriptionDTO;
import com.example.clinic_skin_be.dto.medical.treatment_template.PrescriptionDetailDTO;
import com.example.clinic_skin_be.model.medical.medication.Medication;
import com.example.clinic_skin_be.model.medical.medication.Prescription;
import com.example.clinic_skin_be.model.medical.medication.PrescriptionDetail;
import com.example.clinic_skin_be.repository.medical.medication.IMedicationRepository;
import com.example.clinic_skin_be.service.medical.treatment.PrescriptionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
@AllArgsConstructor
public class PrescriptionRestController {

    private final PrescriptionService prescriptionService;
    private final IMedicationRepository medicationRepository;

    // ------------------- PRESCRIPTION -------------------

    @GetMapping
    public List<PrescriptionDTO> getAllPrescriptions() {
        return prescriptionService.listAllPrescriptions();
    }

    @GetMapping("/{id}")
    public PrescriptionDTO getPrescription(@PathVariable Long id) {
        return prescriptionService.getPrescriptionById(id);
    }

    @PostMapping
    public PrescriptionDTO createPrescription() {
        Prescription prescription = new Prescription();
        return prescriptionService.savePrescription(prescription);
    }

    @DeleteMapping("/{id}")
    public void deletePrescription(@PathVariable Long id) {
        prescriptionService.deletePrescription(id);
    }

    // ------------------- PRESCRIPTION DETAIL -------------------

    @PostMapping("/{prescriptionId}/details")
    public PrescriptionDetailDTO createDetail(
            @PathVariable Long prescriptionId,
            @RequestBody PrescriptionDetailDTO dto
    ) {
        Prescription prescription = new Prescription();
        prescription.setId(prescriptionId);

        Medication medication = medicationRepository.findById(dto.getMedicationId())
                .orElseThrow(() -> new RuntimeException("Medication not found"));

        PrescriptionDetail detail = new PrescriptionDetail();
        detail.setPrescription(prescription);
        detail.setMedication(medication);
        detail.setDosage(dto.getDosage());
        detail.setQuantity(dto.getQuantity());

        return prescriptionService.savePrescriptionDetail(detail);
    }

    @PutMapping("/{prescriptionId}/details/{id}")
    public PrescriptionDetailDTO updateDetail(
            @PathVariable Long prescriptionId,
            @PathVariable Long id,
            @RequestBody PrescriptionDetailDTO dto
    ) {
        Prescription prescription = new Prescription();
        prescription.setId(prescriptionId);

        Medication medication = medicationRepository.findById(dto.getMedicationId())
                .orElseThrow(() -> new RuntimeException("Medication not found"));

        PrescriptionDetail detail = new PrescriptionDetail();
        detail.setId(id);
        detail.setPrescription(prescription);
        detail.setMedication(medication);
        detail.setDosage(dto.getDosage());
        detail.setQuantity(dto.getQuantity());

        return prescriptionService.savePrescriptionDetail(detail);
    }

    @DeleteMapping("/{prescriptionId}/details/{id}")
    public void deleteDetail(@PathVariable Long id) {
        prescriptionService.deletePrescriptionDetail(id);
    }
}
