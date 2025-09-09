// TreatmentTemplateDTO.java
package com.example.clinic_skin_be.dto.medical.treatment_template;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TreatmentTemplateDTO {
    private Long id;
    private String name;
    private String description;
    private String disease_name;
    private List<TreatmentStepTemplateDTO> steps;
}
