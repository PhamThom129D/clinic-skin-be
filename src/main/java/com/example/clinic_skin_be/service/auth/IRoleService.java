package com.example.clinic_skin_be.service.auth;

import com.example.clinic_skin_be.model.admin.Permission;
import com.example.clinic_skin_be.model.user.Role;

import java.util.List;

public interface IRoleService {
    Role getRoleById(Long id);
    Role addPermissionToRole(Long roleId, Long permissionId);
    Role removePermissionFromRole(Long roleId, Long permissionId);
    List<Permission> getAllPermissions();
}

