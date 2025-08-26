package com.example.clinic_skin_be.model;

import jakarta.persistence.*;
import lombok.*;

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
    @Column (name = "appointment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @Column (nullable = false)
    private String time;

    @Column (nullable = false)
    private String date;

    private String note;

    @Enumerated(EnumType.STRING)
    private Status status = Status.Pending;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    public enum Status {
        Pending, Confirmed, Canceled
    }
}

