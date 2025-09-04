package com.example.clinic_skin_be.repository.staff;

import com.example.clinic_skin_be.model.staff.consultant.Consultant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IConsultantRepository extends JpaRepository<Consultant, Long> {
}
