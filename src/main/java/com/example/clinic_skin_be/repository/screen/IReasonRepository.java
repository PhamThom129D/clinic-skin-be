package com.example.clinic_skin_be.repository.screen;

import com.example.clinic_skin_be.model.screen.Reason;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IReasonRepository extends JpaRepository<Reason, Long> {
}
