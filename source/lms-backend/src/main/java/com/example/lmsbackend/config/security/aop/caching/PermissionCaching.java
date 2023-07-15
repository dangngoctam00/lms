package com.example.lmsbackend.config.security.aop.caching;

import com.example.lmsbackend.config.security.aop.PermissionSecurity;
import com.example.lmsbackend.enums.PermissionEnum;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PermissionCaching {

    private Map<Long, Map<PermissionEnum, List<PermissionSecurity>>> permissionOfUserByPermission = new ConcurrentHashMap<>();

    public List<PermissionSecurity> getByPermission(Long userId, PermissionEnum permission) {
        var permissionEnumListMap = permissionOfUserByPermission.get(userId);
        if (permissionEnumListMap == null) {
            return List.of();
        }
        return permissionEnumListMap.get(permission);
    }

    public void addPermission(Long userId, PermissionEnum permissionEnum, List<PermissionSecurity> permissions) {
        var permissionsByPermissionEnum = permissionOfUserByPermission.computeIfAbsent(userId, (Long) -> new HashMap<PermissionEnum, List<PermissionSecurity>>());
        permissionsByPermissionEnum.put(permissionEnum, permissions);
    }
}
