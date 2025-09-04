package com.example.clinic_skin_be.repository.medical;

import com.example.clinic_skin_be.model.medical.StepType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IStepTypeRepository extends JpaRepository<StepType, Long> {
    // Spring Data JPA đã cung cấp sẵn phương thức findById
    // Optional<StepType> findById(Long id); // không cần khai báo cũng có sẵn
}
