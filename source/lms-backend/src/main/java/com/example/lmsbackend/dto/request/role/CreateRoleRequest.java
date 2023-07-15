package com.example.lmsbackend.dto.request.role;

import lombok.Data;

import java.util.List;

@Data
public class CreateRoleRequest {
    private String title;
    private String description;
    private List<PermissionForRole> permissions;
}
