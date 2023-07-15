package com.example.lmsbackend.mapper;

import com.example.lmsbackend.domain.RoleEntity;
import com.example.lmsbackend.dto.response.role.RoleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoleDTOMapper {
    RoleDTOMapper INSTANCE = Mappers.getMapper(RoleDTOMapper.class);

    RoleDTO mapFromRoleEntity(RoleEntity entity);
}
