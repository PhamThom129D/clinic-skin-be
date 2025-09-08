package com.example.clinic_skin_be.mapper;

import com.example.clinic_skin_be.dto.medical.VisitSessionDTO;
import com.example.clinic_skin_be.model.medical.MedicalRecord;
import com.example.clinic_skin_be.model.medical.VisitSession;
import com.example.clinic_skin_be.model.medical.treatment_plan.TreatmentPlan;
import com.example.clinic_skin_be.model.staff.doctor.Doctor;

public class VisitSessionMapper {

    public static VisitSessionDTO toDTO(VisitSession session) {
        if (session == null) return null;

        return VisitSessionDTO.builder()
                .sessionId(session.getSessionId())
                .recordId(session.getMedicalRecord() != null ? session.getMedicalRecord().getRecordId() : null)
                .doctorId(session.getDoctor() != null ? session.getDoctor().getId() : null)
                .doctorName(session.getDoctor() != null ? session.getDoctor().getAccount().getFullName() : null)
                .sessionDate(session.getSessionDate())
                .symptoms(session.getSymptoms())
                .clinicalNotes(session.getClinicalNotes())
                .treatmentPlanId(session.getTreatmentPlan() != null ? session.getTreatmentPlan().getId() : null)
                .treatmentPlanName(session.getTreatmentPlan() != null ? session.getTreatmentPlan().getTreatmentName() : null)
                .createdAt(session.getCreatedAt())
                .updatedAt(session.getUpdatedAt())
                .build();
    }

    public static VisitSession toEntity(VisitSessionDTO dto) {
        if (dto == null) return null;

        VisitSession.VisitSessionBuilder builder = VisitSession.builder()
                .sessionId(dto.getSessionId())
                .sessionDate(dto.getSessionDate())
                .symptoms(dto.getSymptoms())
                .clinicalNotes(dto.getClinicalNotes());

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

        // Gắn treatmentPlan nếu có treatmentPlanId
        if (dto.getTreatmentPlanId() != null) {
            builder.treatmentPlan(TreatmentPlan.builder()
                    .id(dto.getTreatmentPlanId())
                    .build());
        }

        return builder.build();
    }
}
