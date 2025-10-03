package com.example.clinic_skin_be.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class HomeStay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name ;
    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;
    private int quantity_room ;
    private double price;
    private int quantity_room_wc;
    private String description;

}
