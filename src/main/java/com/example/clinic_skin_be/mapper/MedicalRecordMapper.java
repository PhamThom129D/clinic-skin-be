package com.example.clinic_skin_be.mapper;

import com.example.clinic_skin_be.dto.medical.MedicalRecordDTO;
import com.example.clinic_skin_be.model.medical.MedicalRecord;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class MedicalRecordMapper {

    private final VisitSessionMapper visitSessionMapper;

    public MedicalRecordDTO toDTO(MedicalRecord record) {
        if (record == null) return null;

        return MedicalRecordDTO.builder()
                .recordId(record.getRecordId())
                .patientId(record.getPatient() != null ? record.getPatient().getId() : null)
                .patientName(record.getPatient() != null ? record.getPatient().getAccount().getFullName() : null)
                .visitDate(record.getVisitDate())
                .createdAt(record.getCreatedAt())
                .updatedAt(record.getUpdatedAt())
                .visitSessions(record.getVisitSessions() != null
                        ? record.getVisitSessions().stream()
                        .map(visitSessionMapper::toDTO)
                        .collect(Collectors.toList())
                        : null
                )
                .build();
    }
}
