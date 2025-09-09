package com.example.clinic_skin_be.repository.medical;

import com.example.clinic_skin_be.model.medical.MedicalRecord;
import com.example.clinic_skin_be.model.patient.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IMedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {

}
