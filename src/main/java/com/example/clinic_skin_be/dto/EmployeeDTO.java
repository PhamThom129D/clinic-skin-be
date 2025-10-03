// EmployeeDTO.java
package com.example.clinic_skin_be.dto;

import com.example.clinic_skin_be.model.Employee;

public record EmployeeDTO(
        Long id,
        String code,
        String name,
        int age,
        double salary,
        Long departmentId,
        String departmentName
) {
    public static EmployeeDTO from(Employee emp) {
        return new EmployeeDTO(
                emp.getId(),
                emp.getCode(),
                emp.getName(),
                emp.getAge(),
                emp.getSalary(),
                emp.getDepartment() != null ? emp.getDepartment().getId() : null,
                emp.getDepartment() != null ? emp.getDepartment().getName() : null
        );
    }
}
