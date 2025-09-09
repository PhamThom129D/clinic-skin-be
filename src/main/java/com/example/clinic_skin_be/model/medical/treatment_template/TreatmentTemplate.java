package com.example.clinic_skin_be.model.medical.treatment_template;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "treatment_templates") // tên bảng trong DB
public class TreatmentTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name; // tên phác đồ mẫu
    private String disease_name;

    @Column(columnDefinition = "TEXT")
    private String description; // mô tả ngắn gọn về phác đồ

    // Danh sách các bước điều trị của phác đồ
    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TreatmentStepTemplate> steps = new ArrayList<>();
}
