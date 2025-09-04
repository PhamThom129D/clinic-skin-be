package com.example.clinic_skin_be.model.patient;

import com.example.clinic_skin_be.model.user.Account;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "patients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private Long id;

    @Column(name = "passport_number", nullable = false, unique = true)
    private String passportNumber;

    @Column(nullable = false)
    private String occupation;

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;
}

