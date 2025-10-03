package com.example.clinic_skin_be.repository;

import com.example.clinic_skin_be.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDepartmentRepository extends JpaRepository<Department,Long> {
}
