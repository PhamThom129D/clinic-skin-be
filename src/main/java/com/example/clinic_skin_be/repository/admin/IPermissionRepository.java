package com.example.clinic_skin_be.repository.admin;

import com.example.clinic_skin_be.model.admin.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPermissionRepository extends JpaRepository<Permission, Long> {
}
