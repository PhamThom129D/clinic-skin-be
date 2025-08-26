package com.example.clinic_skin_be.model.staff;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    private String fullname;

    private String phone;

    private String email;

    @OneToMany(mappedBy = "consultant")
    @JsonIgnore
    private Set<ConsultationAssignment> assignments = new HashSet<>();

}
