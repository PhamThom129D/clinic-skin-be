package com.example.clinic_skin_be.model;

import jakarta.persistence.*;

@Entity
@Table(name = "lab_staff")
public class LabStaff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "lab_staff_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @OneToOne
    @JoinColumn(name = "account_id", nullable = false, unique = true)
    private Account account;
}

