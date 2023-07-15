package com.example.lmsbackend.mapper;

import com.example.lmsbackend.config.security.aop.PermissionSecurity;
import com.example.lmsbackend.domain.RolePermissionEntity;
import com.example.lmsbackend.domain.UserPermissionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PermissionSecurityMapper {
    PermissionSecurityMapper INSTANCE = Mappers.getMapper(PermissionSecurityMapper.class);

    @Mapping(target = "id", expression = "java(userPermissionEntity.getId().getPermissionId())")
    PermissionSecurity mapFromUserPermissionEntity(UserPermissionEntity userPermissionEntity);

    @Mapping(target = "id", expression = "java(rolePermissionEntity.getId().getPermissionId())")
    PermissionSecurity mapFromRolePermissionEntity(RolePermissionEntity rolePermissionEntity);
}
