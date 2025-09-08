package com.example.clinic_skin_be.model.medical.treatment_plan;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "step_types")
public class StepType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long typeId;

    @Column(name = "type_name", nullable = false)
    private String typeName;// Ví dụ: "Medication", "LabTest", "Procedure"

    private String description;

    private Boolean required = false;

}
