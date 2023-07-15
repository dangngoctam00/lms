package com.example.lmsbackend.mapper;

import com.example.lmsbackend.domain.RolePermissionEntity;
import com.example.lmsbackend.dto.response.role.PermissionOfRoleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PermissionOfRoleDTOMapper {
    PermissionOfRoleDTOMapper INSTANCE = Mappers.getMapper(PermissionOfRoleDTOMapper.class);

    @Mapping(target = "id", expression = "java(rolePermissionEntity.getId().getPermissionId())")
    @Mapping(target = "title", expression = "java(rolePermissionEntity.getPermission().getTitle())")
    @Mapping(target = "code", expression = "java(rolePermissionEntity.getPermission().getCode())")
    @Mapping(target = "description", expression = "java(rolePermissionEntity.getPermission().getDescription())")
    PermissionOfRoleDTO mapFromRolePermissionEntity(RolePermissionEntity rolePermissionEntity);
}
