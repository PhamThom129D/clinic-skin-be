package com.example.clinic_skin_be.model.medical.procedure;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "procedures")
public class Procedure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;   // Tên thủ thuật (vd: Lăn kim, Laser CO2, Peel da...)

    @Column(columnDefinition = "TEXT")
    private String description; // Mô tả ngắn về thủ thuật

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price; // Giá thủ thuật

    @Column(nullable = false)
    private String unit; // Đơn vị tính (lần, buổi, ca...)

    private boolean active = true; // Có đang áp dụng hay không
}
