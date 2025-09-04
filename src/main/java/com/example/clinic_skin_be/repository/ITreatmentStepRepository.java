package com.example.clinic_skin_be.repository;

import com.example.clinic_skin_be.model.medical.TreatmentStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITreatmentStepRepository extends JpaRepository<TreatmentStep, Long> {
    List<TreatmentStep> findByDisease_Name(String diseaseName);
}
