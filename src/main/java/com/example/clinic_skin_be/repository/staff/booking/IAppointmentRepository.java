package com.example.clinic_skin_be.repository.staff.booking;

import com.example.clinic_skin_be.dto.patient.AppointmentHistorySummaryDTO;
import com.example.clinic_skin_be.model.user.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IAppointmentRepository extends JpaRepository<Appointment,Long> {
    List<Appointment> findByDate(LocalDate date);
}
