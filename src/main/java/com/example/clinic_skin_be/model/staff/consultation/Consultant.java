package com.example.clinic_skin_be.model.staff.consultant;

import com.example.clinic_skin_be.model.staff.consultation.ConsultationAssignment;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.example.clinic_skin_be.model.user.Account;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

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
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @OneToMany(mappedBy = "consultant", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<ConsultationAssignment> assignments = new HashSet<>();
}
