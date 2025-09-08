package com.example.clinic_skin_be.model.medical.lab_test;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "lab_tests")
public class LabTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;   // Tên xét nghiệm (vd: Sinh thiết da, Soi tươi nấm, Test dị ứng...)

    @Column(columnDefinition = "TEXT")
    private String description; // Mô tả ngắn về xét nghiệm

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price; // Giá xét nghiệm

    @Column(nullable = false)
    private String unit; // Đơn vị (lần, mẫu, test...)

    private boolean active = true; // Có đang áp dụng hay không
}
