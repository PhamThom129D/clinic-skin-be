package com.example.clinic_skin_be.model.staff.labStaff;

import com.example.clinic_skin_be.model.user.Account;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lab_staff")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabStaff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "lab_staff_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "account_id", nullable = false, unique = true)
    private Account account;
}

