package com.example.clinic_skin_be.service;

import com.example.clinic_skin_be.model.Permission;
import com.example.clinic_skin_be.model.Role;
import com.example.clinic_skin_be.repository.IPermissionRepository;
import com.example.clinic_skin_be.repository.IRoleRepository;
import com.example.clinic_skin_be.service.auth.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService implements IRoleService {

    @Autowired
    private IRoleRepository roleRepo;

    @Autowired
    private IPermissionRepository permRepo;

    @Override
    public Role getRoleById(Long id) {
        return roleRepo.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));
    }

    @Override
    public Role addPermissionToRole(Long roleId, Long permissionId) {
        Role role = getRoleById(roleId);
        Permission perm = permRepo.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permission not found"));
        role.getPermissions().add(perm);
        return roleRepo.save(role);
    }

    @Override
    public Role removePermissionFromRole(Long roleId, Long permissionId) {
        Role role = getRoleById(roleId);
        Permission perm = permRepo.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permission not found"));
        role.getPermissions().remove(perm);
        return roleRepo.save(role);
    }

    @Override
    public List<Permission> getAllPermissions() {
        return permRepo.findAll();
    }
}

