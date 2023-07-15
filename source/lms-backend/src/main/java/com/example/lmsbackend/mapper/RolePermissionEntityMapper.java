package com.example.lmsbackend.mapper;

import com.example.lmsbackend.domain.RolePermissionEntity;
import com.example.lmsbackend.dto.request.role.PermissionForRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RolePermissionEntityMapper {
    RolePermissionEntityMapper INSTANCE = Mappers.getMapper(RolePermissionEntityMapper.class);

    @Mapping(target = "id", ignore = true)
    RolePermissionEntity mapFromPermissionForRole(PermissionForRole permission);
}
