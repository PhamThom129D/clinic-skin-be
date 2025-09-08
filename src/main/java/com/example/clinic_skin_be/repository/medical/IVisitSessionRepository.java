package com.example.clinic_skin_be.repository.medical;

import com.example.clinic_skin_be.model.medical.VisitSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface IVisitSessionRepository extends JpaRepository<VisitSession, Long> {
    List<VisitSession> findByMedicalRecord_RecordId(Long recordId);
    List<VisitSession> findBySessionDateBetween(LocalDateTime start, LocalDateTime end);
}
