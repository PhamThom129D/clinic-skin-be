package com.example.clinic_skin_be.model.staff;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "contacts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contacts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullname;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Enumerated(EnumType.STRING)
    private ConsultationStatus status = ConsultationStatus.PENDING; // e.g., "pending", "in_progress", "completed"

    public enum ConsultationStatus {
        PENDING,    // chưa xử lý
        IN_PROGRESS, // đang xử lý
        COMPLETED        // đã xử lý
    }
}
