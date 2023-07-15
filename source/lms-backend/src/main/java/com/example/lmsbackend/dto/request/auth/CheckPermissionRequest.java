package com.example.lmsbackend.dto.request.auth;

import com.example.lmsbackend.config.security.aop.PermissionSecurity;
import com.example.lmsbackend.enums.PermissionEnum;
import lombok.Data;

@Data
public class CheckPermissionRequest {
    private PermissionEnum permissionName;
    private PermissionSecurity permission;
    private long resourceId;
}
