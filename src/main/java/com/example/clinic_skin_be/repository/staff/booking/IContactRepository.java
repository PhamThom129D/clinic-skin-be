package com.example.clinic_skin_be.repository.staff.booking;

import com.example.clinic_skin_be.model.manage_enum.ConsultationStatus;
import com.example.clinic_skin_be.model.staff.consultation.Contacts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IContactRepository extends JpaRepository<Contacts, Long> {
    List<Contacts> findByStatus(ConsultationStatus status);
}
