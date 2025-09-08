package com.example.clinic_skin_be.repository.medical.treatmnet_template;

import com.example.clinic_skin_be.model.medical.treatment_template.TreatmentStepTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITreatmentStepTemplateRepository extends JpaRepository<TreatmentStepTemplate, Long> {

    // Tìm tất cả các bước theo template ID
    List<TreatmentStepTemplate> findAllByTemplateId(Long templateId);
}
