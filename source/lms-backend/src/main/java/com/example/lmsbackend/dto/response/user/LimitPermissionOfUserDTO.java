package com.example.lmsbackend.dto.response.user;

import com.example.lmsbackend.constant.LimitValue;
import com.example.lmsbackend.domain.RolePermissionEntity;
import com.example.lmsbackend.domain.UserRoleEntity;
import lombok.Data;

import java.lang.reflect.Field;

@Data
public class LimitPermissionOfUserDTO {
    private Integer permissionId;
    private LimitPermission isLimitByBranch;
    private LimitPermission isLimitByTeaching;
    private LimitPermission isLimitByDean;
    private LimitPermission isLimitByManager;
    private LimitPermission isLimitByLearn;

    public void merge(UserRoleEntity userRoleEntity, RolePermissionEntity rolePermissionEntity) throws IllegalAccessException, NoSuchFieldException {
        for (Field field : LimitPermissionOfUserDTO.class.getDeclaredFields()) {
            if (field.getType().equals(LimitPermission.class)) {
                LimitPermission limitPermission = (LimitPermission) field.get(this);
                if (limitPermission != null) {
                    Field rolePermissionField = RolePermissionEntity.class.getDeclaredField(field.getName());
                    rolePermissionField.setAccessible(true);
                    Integer limitValue = (Integer) rolePermissionField.get(rolePermissionEntity);
                    extendLimitTime(userRoleEntity, limitPermission);
                    if (limitValue == LimitValue.UNLIMIT) {
                        extendUnlimitTime(userRoleEntity, limitPermission);
                    }
                }
            }
        }
    }

    private void extendUnlimitTime(UserRoleEntity userRoleEntity, LimitPermission limitPermission) {
        if (limitPermission.getUnlimitStart() == null || limitPermission.getUnlimitStart().isAfter(userRoleEntity.getValidFrom())) {
            limitPermission.setUnlimitStart(userRoleEntity.getValidFrom());
        }
        if (limitPermission.getUnlimitEnd() == null || limitPermission.getUnlimitEnd().isBefore(userRoleEntity.getExpiresAt())) {
            limitPermission.setUnlimitEnd(userRoleEntity.getExpiresAt());
        }
    }

    private void extendLimitTime(UserRoleEntity userRoleEntity, LimitPermission limitPermission) {
        if (limitPermission.getLimitStart().isAfter(userRoleEntity.getValidFrom())) {
            limitPermission.setLimitStart(userRoleEntity.getValidFrom());
        }
        if (limitPermission.getLimitEnd().isBefore(userRoleEntity.getExpiresAt())) {
            limitPermission.setLimitEnd(userRoleEntity.getExpiresAt());
        }
    }
}
