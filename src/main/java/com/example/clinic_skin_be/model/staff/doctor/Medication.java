package com.example.clinic_skin_be.model.staff.doctor;

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

    // Liên kết nhiều thuốc thuộc về 1 bước điều trị
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "step_id", nullable = false)
    private TreatmentStep step;

    @Column(nullable = false)
    private String name;

    private String dosage;

    @Column(name = "usage_instructions", columnDefinition = "TEXT")
    private String usageInstructions;

    // Giá thuốc để tính hóa đơn
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
}
