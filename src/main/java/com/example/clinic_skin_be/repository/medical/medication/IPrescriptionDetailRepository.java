package com.example.clinic_skin_be.repository.medical.medication;

import com.example.clinic_skin_be.model.medical.medication.PrescriptionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPrescriptionDetailRepository extends JpaRepository<PrescriptionDetail,Long> {
}
