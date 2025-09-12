package com.example.clinic_skin_be.model.medical.treatment_plan;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "treatment_plans")
public class TreatmentPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="treatment_name" ,nullable = false)
    private String treatmentName; // Tên phác đồ

    private String disease_name;

    // 1 TreatmentPlan có nhiều bước
    @OneToMany(mappedBy = "treatmentPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TreatmentStep> steps = new ArrayList<>();
}
