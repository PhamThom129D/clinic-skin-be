package com.example.clinic_skin_be.repository.staff.booking;

import com.example.clinic_skin_be.model.user.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAppointmentRepository extends JpaRepository<Appointment,Long> {
}
