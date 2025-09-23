package com.example.clinic_skin_be.model.user;

import com.example.clinic_skin_be.model.manage_enum.ConsultationStatus;
import com.example.clinic_skin_be.model.staff.doctor.Doctor;
import com.example.clinic_skin_be.model.patient.Patient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    @JsonIgnore
    private Patient patient;



    @Column(nullable = false)
    private LocalTime time;

    @Column(nullable = false)
    private LocalDate date;

    private String note;

    @Enumerated(EnumType.STRING)
    private ConsultationStatus status = ConsultationStatus.PENDING;

}
