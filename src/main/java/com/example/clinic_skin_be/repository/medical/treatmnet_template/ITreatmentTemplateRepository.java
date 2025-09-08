package com.example.clinic_skin_be.repository.medical.treatmnet_template;


import com.example.clinic_skin_be.model.medical.treatment_template.TreatmentTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITreatmentTemplateRepository extends JpaRepository<TreatmentTemplate,Long> {
}
