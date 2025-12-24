package com.example.clinic_skin_be.model.staff.cashier;

import com.example.clinic_skin_be.model.user.Account;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "cashiers")
@Data
@Builder
public class Cashier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "cashier_id")
    private Long id;

    @OneToOne
    @JoinColumn (name = "account_id", nullable = false, unique = true)
    private Account account;
}

