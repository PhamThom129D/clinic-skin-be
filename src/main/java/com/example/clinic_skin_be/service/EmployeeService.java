package com.example.clinic_skin_be.service;

import com.example.clinic_skin_be.dto.EmployeeDTO;
import com.example.clinic_skin_be.model.Department;
import com.example.clinic_skin_be.model.Employee;
import com.example.clinic_skin_be.repository.IDepartmentRepository;
import com.example.clinic_skin_be.repository.IEmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EmployeeService {

    private final IDepartmentRepository departmentRepository;
    private final IEmployeeRepository employeeRepository;

    public List<Department> getDepartments(){
        return departmentRepository.findAll();
    }

    // Lấy tất cả employees
    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(EmployeeDTO::from)
                .toList();
    }

    // Lấy employee theo id
    public Optional<EmployeeDTO> getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .map(EmployeeDTO::from);
    }

    // Thêm mới employee
    public EmployeeDTO createEmployee(Employee employee, Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found"));
        employee.setDepartment(department);
        Employee saved = employeeRepository.save(employee);
        return EmployeeDTO.from(saved);
    }

    // Cập nhật employee
    public EmployeeDTO updateEmployee(Long id, Employee employeeDetails, Long departmentId) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        employee.setName(employeeDetails.getName());
        employee.setAge(employeeDetails.getAge());
        employee.setSalary(employeeDetails.getSalary());

        if (departmentId != null) {
            Department department = departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new RuntimeException("Department not found"));
            employee.setDepartment(department);
        }

        Employee updated = employeeRepository.save(employee);
        return EmployeeDTO.from(updated);
    }

    // Xóa employee
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
}
