package com.example.clinic_skin_be.model.screen;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "testimonials")
@Data
public class Testimonial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "testimonial_id")
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    private String img;

    @ManyToOne
    @JoinColumn (name = "account_id", nullable = false)
    private Account account;
}

