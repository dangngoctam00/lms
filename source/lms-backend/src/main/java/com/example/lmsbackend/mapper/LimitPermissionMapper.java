package com.example.lmsbackend.mapper;

import com.example.lmsbackend.domain.RolePermissionEntity;
import com.example.lmsbackend.domain.UserPermissionEntity;
import com.example.lmsbackend.dto.request.auth.LimitPermission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LimitPermissionMapper {
    default LimitPermission mapFromUserPermission(UserPermissionEntity userPermissionEntity) {
        LimitPermission limitPermission = new LimitPermission();
        if (userPermissionEntity.getIsLimitByBranch() == 1) {
            limitPermission.setLimitByBranch(true);
        }
        if (userPermissionEntity.getIsLimitByLearn() == 1) {
            limitPermission.setLimitByLearn(true);
        }
        if (userPermissionEntity.getIsLimitByManager() == 1) {
            limitPermission.setLimitByManager(true);
        }
        if (userPermissionEntity.getIsLimitByTeaching() == 1) {
            limitPermission.setLimitByTeaching(true);
        }
        if (userPermissionEntity.getIsLimitByDean() == 1) {
            limitPermission.setLimitByDean(true);
        }
        return limitPermission;
    }
    default LimitPermission mapFromRolePermission(RolePermissionEntity rolePermissionEntity) {
        LimitPermission limitPermission = new LimitPermission();
        return getLimitPermission(limitPermission, rolePermissionEntity);
    }

    default LimitPermission mapFromLimitPermissionAndRolePermission(LimitPermission limitPermission, RolePermissionEntity rolePermissionEntity){
        return getLimitPermission(limitPermission, rolePermissionEntity);
    }

    private LimitPermission getLimitPermission(LimitPermission limitPermission, RolePermissionEntity rolePermissionEntity) {
        if (rolePermissionEntity.getIsLimitByBranch() == 1) {
            limitPermission.setLimitByBranch(true);
        }
        if (rolePermissionEntity.getIsLimitByLearn() == 1) {
            limitPermission.setLimitByLearn(true);
        }
        if (rolePermissionEntity.getIsLimitByManager() == 1) {
            limitPermission.setLimitByManager(true);
        }
        if (rolePermissionEntity.getIsLimitByTeaching() == 1) {
            limitPermission.setLimitByTeaching(true);
        }
        if (rolePermissionEntity.getIsLimitByDean() == 1) {
            limitPermission.setLimitByDean(true);
        }
        return limitPermission;
    }
}
