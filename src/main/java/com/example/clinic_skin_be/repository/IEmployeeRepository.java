package com.example.clinic_skin_be.repository;

import com.example.clinic_skin_be.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEmployeeRepository extends JpaRepository<Employee, Long> {
}
