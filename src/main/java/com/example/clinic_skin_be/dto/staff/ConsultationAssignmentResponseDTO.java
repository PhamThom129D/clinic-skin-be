package com.example.clinic_skin_be.dto.staff;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultationAssignmentResponseDTO {
    private Long assignmentId;
    private Long contactId;
    private String contactName;
    private Long consultantId;
    private String note;
}
