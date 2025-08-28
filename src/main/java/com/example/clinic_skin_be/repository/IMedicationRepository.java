package com.example.clinic_skin_be.repository;

import com.example.clinic_skin_be.model.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMedicationRepository extends JpaRepository<Medication, Long> {
    List<Medication> findByStep_Disease_Name(String diseaseName);
}
