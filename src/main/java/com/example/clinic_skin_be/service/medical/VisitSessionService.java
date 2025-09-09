package com.example.clinic_skin_be.service.medical;

import com.example.clinic_skin_be.dto.medical.VisitSessionDTO;
import com.example.clinic_skin_be.mapper.VisitSessionMapper;
import com.example.clinic_skin_be.model.medical.MedicalRecord;
import com.example.clinic_skin_be.model.medical.VisitSession;
import com.example.clinic_skin_be.repository.medical.IMedicalRecordRepository;
import com.example.clinic_skin_be.repository.medical.treatment_plan.ITreatmentPlanRepository;
import com.example.clinic_skin_be.repository.medical.IVisitSessionRepository;
import com.example.clinic_skin_be.repository.staff.IDoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class VisitSessionService {

    private final IVisitSessionRepository visitSessionRepo;
    private final IMedicalRecordRepository medicalRecordRepo;
    private final IDoctorRepository doctorRepo;
    private final ITreatmentPlanRepository treatmentPlanRepo;
    private final VisitSessionMapper visitSessionMapper;

    public List<VisitSessionDTO> getSessionsByRecord(Long recordId) {
        return visitSessionRepo.findByMedicalRecord_RecordId(recordId)
                .stream()
                .map(visitSessionMapper::toDTO)
                .collect(Collectors.toList());
    }

    public VisitSessionDTO getSessionById(Long id) {
        return visitSessionRepo.findById(id)
                .map(visitSessionMapper::toDTO)
                .orElse(null);
    }

    public VisitSessionDTO saveVisitSession(Long recordId, VisitSessionDTO dto) {
        MedicalRecord record = medicalRecordRepo.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Medical record not found"));

        VisitSession session;
        if (dto.getSessionId() != null) {
            // update existing
            session = visitSessionRepo.findById(dto.getSessionId())
                    .orElseThrow(() -> new RuntimeException("Visit session not found"));

            session.setSessionDate(dto.getSessionDate());
            session.setSymptoms(dto.getSymptoms());
            session.setClinicalNotes(dto.getClinicalNotes());

            // update treatmentPlan nếu có
            if (dto.getTreatmentPlan().getId() != null) {
                var treatmentPlan = treatmentPlanRepo.findById(dto.getTreatmentPlan().getId())
                        .orElseThrow(() -> new RuntimeException("Treatment plan not found"));
                session.setTreatmentPlan(treatmentPlan);
            }

            session.setUpdatedAt(LocalDateTime.now());

        } else {
            // create new
            session = visitSessionMapper.toEntity(dto);
            session.setMedicalRecord(record);

            // doctor phải có
            if (dto.getDoctorId() != null) {
                var doctor = doctorRepo.findById(dto.getDoctorId())
                        .orElseThrow(() -> new RuntimeException("Doctor not found"));
                session.setDoctor(doctor);
            } else {
                throw new RuntimeException("Doctor is required for visit session");
            }

        }

        VisitSession saved = visitSessionRepo.save(session);

        // đồng bộ với record trong bộ nhớ
        if (!record.getVisitSessions().contains(saved)) {
            record.getVisitSessions().add(saved);
        }

        return visitSessionMapper.toDTO(saved);
    }


    public List<VisitSessionDTO> getSessionByDate(LocalDate localDate) {
        LocalDateTime startOfDay = localDate.atStartOfDay();
        LocalDateTime endOfDay = localDate.atTime(23, 59, 59);

        return visitSessionRepo.findBySessionDateBetween(startOfDay, endOfDay)
                .stream()
                .map(visitSessionMapper::toDTO)
                .collect(Collectors.toList());
    }


    public void deleteVisitSession(Long id) {
        visitSessionRepo.deleteById(id);
    }
}
