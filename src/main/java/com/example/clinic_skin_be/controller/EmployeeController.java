package com.example.clinic_skin_be.controller;

import com.example.clinic_skin_be.dto.EmployeeDTO;
import com.example.clinic_skin_be.model.Department;
import com.example.clinic_skin_be.model.Employee;
import com.example.clinic_skin_be.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@AllArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public List<EmployeeDTO> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/departments")
    public List<Department> getAllDepartments() {
        return employeeService.getDepartments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public EmployeeDTO createEmployee(@RequestBody Employee employee,
                                      @RequestParam Long departmentId) {
        return employeeService.createEmployee(employee, departmentId);
    }

    @PutMapping("/{id}")
    public EmployeeDTO updateEmployee(@PathVariable Long id,
                                      @RequestBody Employee employee,
                                      @RequestParam(required = false) Long departmentId) {
        return employeeService.updateEmployee(id, employee, departmentId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
