package com.example.lmsbackend.dto.request.role;

import lombok.Data;

import java.util.List;

@Data
public class UpdateRoleRequest {
    private Long roleId;
    private String title;
    private String description;
    private List<PermissionForRole> permissions;
}
