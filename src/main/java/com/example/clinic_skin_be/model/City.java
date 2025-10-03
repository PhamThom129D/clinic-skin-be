package com.example.clinic_skin_be.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cities")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String thanhPho;

    @Column(nullable = false, length = 100)
    private String quocGia;

    private Double dienTich;

    private Long danSo;

    private Double gdp;

    @Column(columnDefinition = "TEXT")
    private String moTa;
}
