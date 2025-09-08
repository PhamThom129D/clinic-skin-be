package com.example.clinic_skin_be.model.medical.treatment_plan;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "treatment_steps")
public class TreatmentStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "step_number", nullable = false)
    private int stepNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", nullable = false)
    private StepType stepType; // Loại bước: thuốc/xét nghiệm/thủ thuật

    private Long itemId; // ID của don thuốc/xét nghiệm/thủ thuật tương ứng

    private String notes;   // Ghi chú

    private String results; // Kết quả sau khi thực hiện bước

    // Liên kết ngược về TreatmentPlan
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private TreatmentPlan treatmentPlan;
}
