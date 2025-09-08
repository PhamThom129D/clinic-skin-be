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

    // Convert PrescriptionDetail -> DTO
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


    // Convert Prescription -> DTO
    public PrescriptionDTO toDTO(Prescription prescription) {
        if (prescription == null) return null;
        List<PrescriptionDetailDTO> detailsDTO = prescription.getDetails().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return new PrescriptionDTO(prescription.getId(), prescription.getCreatedAt(), detailsDTO);
    }

    // Convert list
    public List<PrescriptionDTO> toDTO(List<Prescription> prescriptions) {
        return prescriptions.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<PrescriptionDetailDTO> toDetailDTO(List<PrescriptionDetail> details) {
        return details.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
