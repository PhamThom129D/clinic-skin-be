package com.example.clinic_skin_be.repository.medical;

import com.example.clinic_skin_be.model.medical.VisitSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface IVisitSessionRepository extends JpaRepository<VisitSession, Long> {
    List<VisitSession> findAllByMedicalRecord_RecordId(Long recordId);
    List<VisitSession> findBySessionDateBetween(LocalDateTime start, LocalDateTime end);
    Optional<VisitSession> findFirstByMedicalRecord_RecordId(Long recordId);
}
