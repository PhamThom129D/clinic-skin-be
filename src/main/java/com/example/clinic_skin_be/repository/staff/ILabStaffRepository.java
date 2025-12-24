package com.example.clinic_skin_be.repository.staff;

import com.example.clinic_skin_be.model.staff.labStaff.LabStaff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ILabStaffRepository extends JpaRepository<LabStaff, Long> {
}
