package com.example.clinic_skin_be.dto.medical.treatment_template;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrescriptionDTO {
    private Long id;
    private LocalDateTime createdAt;
    private List<PrescriptionDetailDTO> details;
}


