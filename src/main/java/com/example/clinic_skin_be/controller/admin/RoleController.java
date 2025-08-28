package com.example.clinic_skin_be.controller.admin;

import com.example.clinic_skin_be.model.admin.Permission;
import com.example.clinic_skin_be.model.user.Role;
import com.example.clinic_skin_be.service.admin.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/roles")
@PreAuthorize("hasRole('ADMIN')")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("/{roleId}/permissions/{permissionId}")
    public Role addPermission(@PathVariable Long roleId, @PathVariable Long permissionId) {
        return roleService.addPermissionToRole(roleId, permissionId);
    }

    @DeleteMapping("/{roleId}/permissions/{permissionId}")
    public Role removePermission(@PathVariable Long roleId, @PathVariable Long permissionId) {
        return roleService.removePermissionFromRole(roleId, permissionId);
    }

    @GetMapping("/permissions")
    public List<Permission> listPermissions() {
        return roleService.getAllPermissions();
    }

    @GetMapping("/{roleId}")
    public Role getRole(@PathVariable Long roleId) {
        return roleService.getRoleById(roleId);
    }
}
