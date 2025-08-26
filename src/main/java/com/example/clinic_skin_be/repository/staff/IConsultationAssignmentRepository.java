package com.example.clinic_skin_be.repository.staff;

import com.example.clinic_skin_be.model.staff.ConsultationAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IConsultationAssignmentRepository extends JpaRepository<ConsultationAssignment,Long> {
}
