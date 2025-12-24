package com.example.clinic_skin_be.repository.staff.booking;

import com.example.clinic_skin_be.dto.patient.AppointmentHistorySummaryDTO;
import com.example.clinic_skin_be.model.manage_enum.ConsultationStatus;
import com.example.clinic_skin_be.model.user.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface IAppointmentRepository extends JpaRepository<Appointment,Long> {
    List<Appointment> findByDate(LocalDate date);
    Optional<Appointment> findFirstByPatient_AccountIdAndDateAndStatusIn(Long accountId, LocalDate date, Collection<ConsultationStatus> statuses);
}
