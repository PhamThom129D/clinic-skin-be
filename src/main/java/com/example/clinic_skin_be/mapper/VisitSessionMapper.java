package com.example.clinic_skin_be.mapper;

import com.example.clinic_skin_be.dto.medical.VisitSessionDTO;
import com.example.clinic_skin_be.model.medical.VisitSession;

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
                .diagnosis(session.getDiagnosis())
                .treatmentPlan(session.getTreatmentPlan())
                .prescriptions(session.getPrescriptions())
                .labTests(session.getLabTests())
                .followUpDate(session.getFollowUpDate())
                .progressNotes(session.getProgressNotes())
                .createdAt(session.getCreatedAt())
                .updatedAt(session.getUpdatedAt())
                .build();
    }

public static VisitSession toEntity(VisitSessionDTO dto) {
        if (dto == null) return null;
        return VisitSession.builder()
                .sessionId(dto.getSessionId())
                .sessionDate(dto.getSessionDate())
                .symptoms(dto.getSymptoms())
                .clinicalNotes(dto.getClinicalNotes())
                .diagnosis(dto.getDiagnosis())
                .treatmentPlan(dto.getTreatmentPlan())
                .prescriptions(dto.getPrescriptions())
                .labTests(dto.getLabTests())
                .followUpDate(dto.getFollowUpDate())
                .progressNotes(dto.getProgressNotes())
                .build();
    }
}
