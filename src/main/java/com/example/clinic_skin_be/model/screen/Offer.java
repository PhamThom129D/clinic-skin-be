package com.example.clinic_skin_be.model.screen;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "offers")
@Data
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "offer_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;


    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String img;
}
