package com.example.clinic_skin_be.repository;

import com.example.clinic_skin_be.model.staff.doctor.Disease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IDiseaseRepository extends JpaRepository<Disease, Long> {
    Optional<Disease> findByName(String name);
}