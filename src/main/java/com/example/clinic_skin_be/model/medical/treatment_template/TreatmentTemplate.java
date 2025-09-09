package com.example.clinic_skin_be.model.medical.treatment_template;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "treatment_templates") // tên bảng trong DB
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TreatmentTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name; // tên phác đồ mẫu

    @Column(columnDefinition = "TEXT")
    private String description; // mô tả ngắn gọn về phác đồ

    @Column(name = "disease_name") // map với cột trong DB
    private String diseaseName;   // dùng camelCase cho Java code

    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TreatmentStepTemplate> steps = new ArrayList<>();
}
