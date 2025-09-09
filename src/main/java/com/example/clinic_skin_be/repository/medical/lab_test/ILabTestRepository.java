package com.example.clinic_skin_be.repository.medical.lab_test;

import com.example.clinic_skin_be.model.medical.lab_test.LabTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ILabTestRepository extends JpaRepository<LabTest,Long> {
}
