package com.example.clinic_skin_be.repository.staff;

import com.example.clinic_skin_be.model.staff.doctor.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDoctorRepository extends JpaRepository<Doctor, Long> {
}
