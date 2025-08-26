package com.example.clinic_skin_be.dto.staff;

import com.example.clinic_skin_be.model.manage_enum.ConsultationStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultationAssignmentDTO {

    private Long contactId;       // ID của contact cần gán tư vấn viên
    private Long consultantId;    // ID của consultant được gán
    private String note;          // Ghi chú cho assignment
    private ConsultationStatus status; // Trạng thái contact, có thể null nếu không cập nhật
}
