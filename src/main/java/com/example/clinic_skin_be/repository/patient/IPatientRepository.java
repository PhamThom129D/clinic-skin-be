package com.example.clinic_skin_be.repository.patient;

import com.example.clinic_skin_be.model.patient.Patient;
import com.example.clinic_skin_be.model.user.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IPatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByAccount(Account account);
}
