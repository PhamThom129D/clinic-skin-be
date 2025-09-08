package com.example.clinic_skin_be.repository.medical.medication;

import com.example.clinic_skin_be.model.medical.medication.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPrescriptionRepository extends JpaRepository<Prescription,Long> {
}
