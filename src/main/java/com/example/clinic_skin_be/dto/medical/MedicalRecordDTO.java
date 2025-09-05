package com.example.clinic_skin_be.dto.medical;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecordDTO {
    private Long recordId;

    private Long patientId;
    private String patientName;

    private Long doctorId;
    private String doctorName;

    private LocalDate visitDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // NEW: danh sách các phiên khám
    private List<VisitSessionDTO> visitSessions;
}
