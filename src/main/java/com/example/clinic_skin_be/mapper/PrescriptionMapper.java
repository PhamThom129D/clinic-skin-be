package com.example.clinic_skin_be.mapper;

import com.example.clinic_skin_be.dto.medical.treatment_template.PrescriptionDTO;
import com.example.clinic_skin_be.dto.medical.treatment_template.PrescriptionDetailDTO;
import com.example.clinic_skin_be.model.medical.medication.Medication;
import com.example.clinic_skin_be.model.medical.medication.Prescription;
import com.example.clinic_skin_be.model.medical.medication.PrescriptionDetail;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
@Component
public class PrescriptionMapper {

    // ----------------- DTO -> Entity -----------------

    public PrescriptionDetail fromDTO(PrescriptionDetailDTO dto, Prescription prescription, Medication medication) {
        if (dto == null) return null;
        PrescriptionDetail detail = new PrescriptionDetail();
        detail.setId(dto.getId());
        detail.setPrescription(prescription);
        detail.setMedication(medication);
        detail.setDosage(dto.getDosage());
        detail.setQuantity(dto.getQuantity());
        detail.setInstructions(dto.getInstructions());
        return detail;
    }

    public Prescription fromDTO(PrescriptionDTO dto) {
        if (dto == null) return null;
        Prescription prescription = new Prescription();
        prescription.setId(dto.getId());
        prescription.setCreatedAt(dto.getCreatedAt());
        // Chi tiết có thể add sau khi map medication
        return prescription;
    }

    public List<Prescription> fromDTO(List<PrescriptionDTO> dtos) {
        return dtos.stream()
                .map(this::fromDTO)
                .collect(Collectors.toList());
    }

    public List<PrescriptionDetail> fromDetailDTO(List<PrescriptionDetailDTO> dtos, Prescription prescription, List<Medication> medications) {
        return dtos.stream()
                .map(dto -> {
                    Medication med = medications.stream()
                            .filter(m -> m.getId().equals(dto.getMedicationId()))
                            .findFirst()
                            .orElse(null);
                    return fromDTO(dto, prescription, med);
                })
                .collect(Collectors.toList());
    }

    // ----------------- DTO <- Entity -----------------

    public PrescriptionDetailDTO toDTO(PrescriptionDetail detail) {
        if (detail == null) return null;
        Medication med = detail.getMedication();
        return new PrescriptionDetailDTO(
                detail.getId(),
                med != null ? med.getId() : null,
                med != null ? med.getName() : null,
                detail.getDosage(),
                detail.getQuantity(),
                med != null ? med.getUnit() : null,
                med != null && med.getPrice() != null ? med.getPrice().toString() : null,
                detail.getInstructions() != null ? detail.getInstructions() : null
        );
    }

    public PrescriptionDTO toDTO(Prescription prescription) {
        if (prescription == null) return null;
        List<PrescriptionDetailDTO> detailsDTO = prescription.getDetails().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return new PrescriptionDTO(prescription.getId(), prescription.getCreatedAt(), detailsDTO);
    }

    public List<PrescriptionDTO> toDTO(List<Prescription> prescriptions) {
        return prescriptions.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<PrescriptionDetailDTO> toDetailDTO(List<PrescriptionDetail> details) {
        return details.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
