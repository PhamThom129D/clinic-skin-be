package com.example.clinic_skin_be.repository.staff;

import com.example.clinic_skin_be.model.staff.doctor.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDoctorRepository extends JpaRepository<Doctor, Long> {
}
