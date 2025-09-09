package com.example.clinic_skin_be.dto.medical;

import com.example.clinic_skin_be.dto.medical.treatment_template.TreatmentTemplateDTO;
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
    private String patientName;

    private Long doctorId;
    private String doctorName;

    private LocalDateTime sessionDate;
    private String symptoms;
    private String clinicalNotes;

    private TreatmentTemplateDTO treatmentPlan;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
