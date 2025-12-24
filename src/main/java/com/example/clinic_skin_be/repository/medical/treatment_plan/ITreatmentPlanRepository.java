package com.example.clinic_skin_be.repository.medical.treatment_plan;
import com.example.clinic_skin_be.model.medical.treatment_plan.TreatmentPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITreatmentPlanRepository extends JpaRepository<TreatmentPlan, Long> {
//    Optional<TreatmentPlan> findByName(String name);
}