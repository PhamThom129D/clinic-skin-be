package com.example.clinic_skin_be.model.medical.medication;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "medications")
public class Medication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;   // Tên thuốc

    @Column(nullable = false)
    private String unit;   // Đơn vị tính (viên, lọ, ml...)

    private int stock;     // Số lượng tồn kho

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price; // Giá thuốc
}
