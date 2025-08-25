package com.example.clinic_skin_be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RolePermissionDTO {
    private String screenName;
    private String screenDisplayName;
    private boolean canView;
    private boolean canCreate;
    private boolean canEdit;
    private boolean canDelete;
}
