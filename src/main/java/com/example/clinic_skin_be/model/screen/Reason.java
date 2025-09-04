package com.example.clinic_skin_be.model.screen;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "reasons")
@Data
public class Reason {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "reason_id")
    private Long id;

    @Column (nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column (nullable = false)
    private String img;
}

