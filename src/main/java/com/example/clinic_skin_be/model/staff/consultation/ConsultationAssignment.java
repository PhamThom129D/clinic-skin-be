package com.example.clinic_skin_be.model.staff.consultation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import com.example.clinic_skin_be.model.staff.consultant.Consultant;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "consultation_assignments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultationAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "contact_id")
    @JsonIgnore
    private Contacts contact;

    @ManyToOne
    @JoinColumn(name = "consultant_id")
    @JsonIgnore
    private Consultant consultant;

    @Column(length = 10000)
    private String note;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
