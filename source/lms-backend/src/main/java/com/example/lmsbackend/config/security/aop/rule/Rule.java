package com.example.lmsbackend.config.security.aop.rule;

import com.example.lmsbackend.config.security.CustomUserDetails;
import com.example.lmsbackend.config.security.aop.PermissionSecurity;
import com.example.lmsbackend.enums.ResourceType;

public interface Rule {
    ResourceType getResourceType();
    default boolean authorize(CustomUserDetails currUser, PermissionSecurity permission, long resourceId) {
        return true;
    }

    default boolean authorize(CustomUserDetails currUser, PermissionSecurity permission, String resourceId) {
        return true;
    }
}
