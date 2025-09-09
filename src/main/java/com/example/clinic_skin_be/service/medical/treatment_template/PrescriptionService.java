package com.example.clinic_skin_be.service.medical.treatment_template;

import com.example.clinic_skin_be.dto.medical.treatment_template.PrescriptionDTO;
import com.example.clinic_skin_be.dto.medical.treatment_template.PrescriptionDetailDTO;
import com.example.clinic_skin_be.mapper.PrescriptionMapper;
import com.example.clinic_skin_be.model.medical.medication.Prescription;
import com.example.clinic_skin_be.model.medical.medication.PrescriptionDetail;
import com.example.clinic_skin_be.repository.medical.medication.IMedicationRepository;
import com.example.clinic_skin_be.repository.medical.medication.IPrescriptionDetailRepository;
import com.example.clinic_skin_be.repository.medical.medication.IPrescriptionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PrescriptionService {

    private final IPrescriptionRepository prescriptionRepository;
    private final IPrescriptionDetailRepository detailRepository;
    private final IMedicationRepository medicationRepository;
    private final PrescriptionMapper mapper;

    // ------------------- PRESCRIPTION -------------------

    public List<PrescriptionDTO> listAllPrescriptions() {
        return mapper.toDTO(prescriptionRepository.findAll());
    }

    public PrescriptionDTO getPrescriptionById(Long id) {
        return prescriptionRepository.findById(id).map(mapper::toDTO).orElse(null);
    }

    public PrescriptionDTO savePrescription(Prescription prescription) {
        Prescription saved = prescriptionRepository.save(prescription);
        return mapper.toDTO(saved);
    }

    public void deletePrescription(Long id) {
        prescriptionRepository.deleteById(id);
    }

    // ------------------- PRESCRIPTION DETAIL -------------------

    public PrescriptionDetailDTO savePrescriptionDetail(PrescriptionDetail detail) {
        PrescriptionDetail saved = detailRepository.save(detail);
        return mapper.toDTO(saved);
    }

    public void deletePrescriptionDetail(Long id) {
        detailRepository.deleteById(id);
    }
}
