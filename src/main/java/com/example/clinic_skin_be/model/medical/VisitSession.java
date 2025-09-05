package com.example.clinic_skin_be.model.medical;

import com.example.clinic_skin_be.model.staff.doctor.Doctor;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "visit_sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class VisitSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Long sessionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id", nullable = false)
    @JsonBackReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private MedicalRecord medicalRecord;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(name = "session_date", nullable = false)
    private LocalDateTime sessionDate;

    @Column(columnDefinition = "TEXT")
    private String symptoms;

    @Column(name = "clinical_notes", columnDefinition = "TEXT")
    private String clinicalNotes;

    @Column(columnDefinition = "TEXT")
    private String diagnosis;

    @Column(name = "treatment_plan", columnDefinition = "TEXT")
    private String treatmentPlan;

    @Column(columnDefinition = "TEXT")
    private String prescriptions;

    @Column(name = "lab_tests", columnDefinition = "TEXT")
    private String labTests;

    @Column(name = "follow_up_date")
    private LocalDate followUpDate;

    @Column(name = "progress_notes", columnDefinition = "TEXT")
    private String progressNotes;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
