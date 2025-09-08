package com.example.clinic_skin_be.dto.medical;

import lombok.*;

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

    private Long treatmentPlanId;
    private String treatmentPlanName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
