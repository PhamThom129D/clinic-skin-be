package com.example.clinic_skin_be.model.staff.doctor;

import com.example.clinic_skin_be.model.user.Account;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "doctors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id")
    private Long id;

    @Column (nullable = false)
    private String specialty;

    @Column(nullable = false)
    private String level;

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany (
            mappedBy = "doctor",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private Set<Certificate> certificates = new HashSet<>();
}

