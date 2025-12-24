package com.example.clinic_skin_be.repository.staff;

import com.example.clinic_skin_be.model.staff.doctor.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICertificateRepository extends JpaRepository<Certificate, Long> {
    List<Certificate> findByDoctor_Id(Long doctorId);
}
