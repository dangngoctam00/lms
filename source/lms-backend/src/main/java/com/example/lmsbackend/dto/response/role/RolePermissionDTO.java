package com.example.lmsbackend.dto.response.role;

import lombok.Data;

import java.util.List;

@Data
public class RolePermissionDTO {
    private Long id;
    private String description;
    private String title;
    private List<PermissionOfRoleDTO> permissions;
}
