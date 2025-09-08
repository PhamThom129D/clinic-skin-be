package com.example.clinic_skin_be.dto.medical.treatment_template;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrescriptionDetailDTO {
    private Long id;
    private Long medicationId;
    private String medicationName;
    private String dosage;
    private int quantity;
    private String unit;
    private String price;
    private String instructions;
}
