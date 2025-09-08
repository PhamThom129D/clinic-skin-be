// TreatmentStepTemplateDTO.java
package com.example.clinic_skin_be.dto.medical.treatment_template;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TreatmentStepTemplateDTO {
    private Long id;
    private int stepNumber;
    private Long stepTypeId; // ID loại bước
    private String stepTypeName; // tên loại bước
    private String stepDesc; // tên loại bước
    private Long itemId; // ID don thuốc/xét nghiệm/thủ thuật
    private String notes;
    private Long templateId; // ID phác đồ cha
    private String templateName;
}
