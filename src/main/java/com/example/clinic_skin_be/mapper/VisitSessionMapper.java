package com.example.clinic_skin_be.mapper;

import com.example.clinic_skin_be.dto.medical.VisitSessionDTO;
import com.example.clinic_skin_be.model.medical.MedicalRecord;
import com.example.clinic_skin_be.model.medical.VisitSession;
import com.example.clinic_skin_be.model.medical.treatment_plan.TreatmentPlan;
import com.example.clinic_skin_be.model.staff.doctor.Doctor;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class VisitSessionMapper {

    private final TreatmentPlanMapper treatmentPlanMapper;

    // ===============================
    // Map VisitSession -> VisitSessionDTO
    // ===============================
    public VisitSessionDTO toDTO(VisitSession session) {
        if (session == null) return null;

        return VisitSessionDTO.builder()
                .sessionId(session.getSessionId())
                .recordId(session.getMedicalRecord() != null ? session.getMedicalRecord().getRecordId() : null)
                .patientName(session.getMedicalRecord() != null && session.getMedicalRecord().getPatient() != null
                        ? session.getMedicalRecord().getPatient().getAccount().getFullName()
                        : null)
                .doctorId(session.getDoctor() != null ? session.getDoctor().getId() : null)
                .doctorName(session.getDoctor() != null ? session.getDoctor().getAccount().getFullName() : null)
                .sessionDate(session.getSessionDate())
                .symptoms(session.getSymptoms())
                .diagnosis(session.getDiagnosis())
                .clinicalNotes(session.getClinicalNotes())
                .treatmentPlan(treatmentPlanMapper.toDTO(session.getTreatmentPlan()))
                .createdAt(session.getCreatedAt())
                .updatedAt(session.getUpdatedAt())
                .build();
    }

    // ===============================
    // Map VisitSessionDTO -> VisitSession
    // ===============================
    public VisitSession toEntity(VisitSessionDTO dto) {
        if (dto == null) return null;

        VisitSession.VisitSessionBuilder builder = VisitSession.builder()
                .sessionId(dto.getSessionId())
                .sessionDate(dto.getSessionDate())
                .symptoms(dto.getSymptoms())
                .diagnosis(dto.getDiagnosis())
                .clinicalNotes(dto.getClinicalNotes())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt());

        // Gắn medicalRecord nếu có recordId
        if (dto.getRecordId() != null) {
            builder.medicalRecord(MedicalRecord.builder()
                    .recordId(dto.getRecordId())
                    .build());
        }

        // Gắn doctor nếu có doctorId
        if (dto.getDoctorId() != null) {
            builder.doctor(Doctor.builder()
                    .id(dto.getDoctorId())
                    .build());
        }

        // Gắn treatmentPlan nếu có DTO
        if (dto.getTreatmentPlan() != null && dto.getTreatmentPlan().getId() != null) {
            builder.treatmentPlan(TreatmentPlan.builder()
                    .id(dto.getTreatmentPlan().getId())
                    .build());
        }

        return builder.build();
    }
}
