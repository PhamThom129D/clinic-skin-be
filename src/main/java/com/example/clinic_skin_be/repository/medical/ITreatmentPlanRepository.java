package com.example.clinic_skin_be.repository.medical;
import com.example.clinic_skin_be.model.medical.treatment_plan.TreatmentPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ITreatmentPlanRepository extends JpaRepository<TreatmentPlan, Long> {
//    Optional<TreatmentPlan> findByName(String name);
}