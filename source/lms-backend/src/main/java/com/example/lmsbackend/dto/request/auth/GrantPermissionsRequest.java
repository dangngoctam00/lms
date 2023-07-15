package com.example.lmsbackend.dto.request.auth;

import com.example.lmsbackend.dto.response.auth.GrantedPermission;
import com.example.lmsbackend.dto.response.auth.GrantedRole;
import lombok.Data;

import java.util.List;

@Data
public class GrantPermissionsRequest {
    private Long userId;
    private List<GrantedRole> roles;
    private List<GrantedPermission> permissions;
}
