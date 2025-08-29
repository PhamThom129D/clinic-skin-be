package com.example.clinic_skin_be.repository.staff;

import com.example.clinic_skin_be.model.staff.doctor.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public interface IDoctorRepository extends JpaRepository<Doctor, Long> {
    @Query("SELECT d.id AS doctorId, d.account.fullName AS fullName, d.specialty AS specialty, d.account.avtPath AS avtPath FROM Doctor d")
    List<Map<String, Object>> findAllDoctorInfo();
}
