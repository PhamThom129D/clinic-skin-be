package com.example.clinic_skin_be.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "consultants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Consultant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consultant_id")
    private Long id;

    @Column(name = "customer_count")
    private int customerCount;

    private String note;

    @OneToOne
    @JoinColumn (name = "account_id", nullable = false, unique = true)
    private Account account;
}

