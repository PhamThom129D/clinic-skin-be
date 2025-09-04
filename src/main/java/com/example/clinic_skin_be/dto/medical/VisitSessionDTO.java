package com.example.clinic_skin_be.dto.medical;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisitSessionDTO {
    private Long sessionId;
    private Long recordId;

    private Long doctorId;
    private String doctorName;

    private LocalDateTime sessionDate;
    private String symptoms;
    private String clinicalNotes;
    private String diagnosis;
    private String treatmentPlan;
    private String prescriptions;
    private String labTests;
    private LocalDate followUpDate;
    private String progressNotes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
