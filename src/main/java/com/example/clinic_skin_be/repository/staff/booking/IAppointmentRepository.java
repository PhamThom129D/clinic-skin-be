package com.example.clinic_skin_be.repository.staff.booking;

import com.example.clinic_skin_be.model.user.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface IAppointmentRepository extends JpaRepository<Appointment,Long> {
    List<Appointment> findByDate(LocalDate date);
}
