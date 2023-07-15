package com.example.lmsbackend.dto.response.user;

import com.example.lmsbackend.constant.LimitValue;
import com.example.lmsbackend.domain.RolePermissionEntity;
import com.example.lmsbackend.domain.UserRoleEntity;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class LimitPermissionOfUserDTOTest {

    @Test
    void merge() throws NoSuchFieldException, IllegalAccessException {
        // given
        LimitPermissionOfUserDTO limitPermissionOfUserDTO = new LimitPermissionOfUserDTO();
        limitPermissionOfUserDTO.setIsLimitByManager(
                LimitPermission.builder()
                        .limitStart(LocalDateTime.of(2022,3,10,10,10,10))
                        .limitEnd(LocalDateTime.of(2022,4,10,10,10,10))
                .build()
        );

        UserRoleEntity userRoleEntity = new UserRoleEntity();
        userRoleEntity.setValidFrom(LocalDateTime.of(2022,2,10,10,10,10));
        userRoleEntity.setExpiresAt(LocalDateTime.of(2022,6,10,10,10,10));

        RolePermissionEntity rolePermissionEntity = new RolePermissionEntity();
        rolePermissionEntity.setIsLimitByManager(LimitValue.UNLIMIT);
        // when
        limitPermissionOfUserDTO.merge(userRoleEntity, rolePermissionEntity);
        // then
        AssertionsForClassTypes.assertThat(limitPermissionOfUserDTO.getIsLimitByManager())
                .isEqualTo(LimitPermission.builder()
                        .limitStart(userRoleEntity.getValidFrom())
                        .limitEnd(userRoleEntity.getExpiresAt())
                        .unlimitStart(userRoleEntity.getValidFrom())
                        .unlimitEnd(userRoleEntity.getExpiresAt())
                        .build()
                );
    }
}