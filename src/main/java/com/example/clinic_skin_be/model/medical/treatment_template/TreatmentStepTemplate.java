package com.example.clinic_skin_be.model.medical.treatment_template;

import com.example.clinic_skin_be.model.medical.treatment_plan.StepType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "treatment_step_templates")
public class TreatmentStepTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "step_number", nullable = false)
    private int stepNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", nullable = false)
    private StepType stepType; // Loại bước: thuốc/xét nghiệm/thủ thuật

    private Long itemId; // ID thuốc/xét nghiệm/thủ thuật tương ứng

    private String notes;

    // Liên kết ngược về phác đồ mẫu
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    private TreatmentTemplate template; // **mappedBy phải trùng tên này**
}
