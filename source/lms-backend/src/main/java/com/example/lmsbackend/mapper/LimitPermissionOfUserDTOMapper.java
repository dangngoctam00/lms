package com.example.lmsbackend.mapper;

import com.example.lmsbackend.constant.LimitValue;
import com.example.lmsbackend.domain.UserPermissionEntity;
import com.example.lmsbackend.dto.response.user.LimitPermission;
import com.example.lmsbackend.dto.response.user.LimitPermissionOfUserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LimitPermissionOfUserDTOMapper {
    LimitPermissionOfUserDTOMapper INSTANCE = Mappers.getMapper(LimitPermissionOfUserDTOMapper.class);

    default LimitPermissionOfUserDTO mapFromPermissionEntity(UserPermissionEntity entity) {
        LimitPermissionOfUserDTO limitPermissionOfUserDTO = new LimitPermissionOfUserDTO();
        limitPermissionOfUserDTO.setPermissionId(entity.getId().getPermissionId());

        // branch
        if (entity.getIsLimitByBranch() == LimitValue.LIMIT) {
            limitPermissionOfUserDTO.setIsLimitByBranch(
                    LimitPermission.builder()
                            .limitStart(entity.getValidFrom())
                            .limitEnd(entity.getExpiresAt())
                            .build()
            );
        } else if (entity.getIsLimitByBranch() == LimitValue.UNLIMIT) {
            limitPermissionOfUserDTO.setIsLimitByBranch(
                    LimitPermission.builder()
                            .limitStart(entity.getValidFrom())
                            .limitEnd(entity.getExpiresAt())
                            .unlimitStart(entity.getValidFrom())
                            .unlimitEnd(entity.getExpiresAt())
                            .build()
            );
        }

        // teaching
        if (entity.getIsLimitByTeaching() == LimitValue.LIMIT) {
            limitPermissionOfUserDTO.setIsLimitByTeaching(
                    LimitPermission.builder()
                            .limitStart(entity.getValidFrom())
                            .limitEnd(entity.getExpiresAt())
                            .build()
            );
        } else if (entity.getIsLimitByTeaching() == LimitValue.UNLIMIT) {
            limitPermissionOfUserDTO.setIsLimitByTeaching(
                    LimitPermission.builder()
                            .limitStart(entity.getValidFrom())
                            .limitEnd(entity.getExpiresAt())
                            .unlimitStart(entity.getValidFrom())
                            .unlimitEnd(entity.getExpiresAt())
                            .build()
            );
        }

        // dean
        if (entity.getIsLimitByDean() == LimitValue.LIMIT) {
            limitPermissionOfUserDTO.setIsLimitByDean(
                    LimitPermission.builder()
                            .limitStart(entity.getValidFrom())
                            .limitEnd(entity.getExpiresAt())
                            .build()
            );
        } else if (entity.getIsLimitByDean() == LimitValue.UNLIMIT) {
            limitPermissionOfUserDTO.setIsLimitByDean(
                    LimitPermission.builder()
                            .limitStart(entity.getValidFrom())
                            .limitEnd(entity.getExpiresAt())
                            .unlimitStart(entity.getValidFrom())
                            .unlimitEnd(entity.getExpiresAt())
                            .build()
            );
        }

        // manager
        if (entity.getIsLimitByManager() == LimitValue.LIMIT) {
            limitPermissionOfUserDTO.setIsLimitByManager(
                    LimitPermission.builder()
                            .limitStart(entity.getValidFrom())
                            .limitEnd(entity.getExpiresAt())
                            .build()
            );
        } else if (entity.getIsLimitByManager() == LimitValue.UNLIMIT) {
            limitPermissionOfUserDTO.setIsLimitByManager(
                    LimitPermission.builder()
                            .limitStart(entity.getValidFrom())
                            .limitEnd(entity.getExpiresAt())
                            .unlimitStart(entity.getValidFrom())
                            .unlimitEnd(entity.getExpiresAt())
                            .build()
            );
        }

        // learn
        if (entity.getIsLimitByLearn() == LimitValue.LIMIT) {
            limitPermissionOfUserDTO.setIsLimitByLearn(
                    LimitPermission.builder()
                            .limitStart(entity.getValidFrom())
                            .limitEnd(entity.getExpiresAt())
                            .build()
            );
        } else if (entity.getIsLimitByLearn() == LimitValue.UNLIMIT) {
            limitPermissionOfUserDTO.setIsLimitByLearn(
                    LimitPermission.builder()
                            .limitStart(entity.getValidFrom())
                            .limitEnd(entity.getExpiresAt())
                            .unlimitStart(entity.getValidFrom())
                            .unlimitEnd(entity.getExpiresAt())
                            .build()
            );
        }

        return limitPermissionOfUserDTO;
    }
}
