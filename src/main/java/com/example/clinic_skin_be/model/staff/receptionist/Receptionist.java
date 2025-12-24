package com.example.clinic_skin_be.model.staff.receptionist;

import com.example.clinic_skin_be.model.user.Account;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "receptionists")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Receptionist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "receptionist_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;
}

