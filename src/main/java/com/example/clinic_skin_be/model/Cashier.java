package com.example.clinic_skin_be.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "cashiers")
@Data
public class Cashier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "cashier_id")
    private Long id;

    @OneToOne
    @JoinColumn (name = "account_id", nullable = false, unique = true)
    private Account account;
}

