package com.example.lmsbackend.mapper;

import com.example.lmsbackend.domain.UserPermissionEntity;
import com.example.lmsbackend.dto.response.user.PermissionOfUserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PermissionOfUserDTOMapper {
    PermissionOfUserDTOMapper INSTANCE = Mappers.getMapper(PermissionOfUserDTOMapper.class);

    @Mapping(target = "id", expression = "java(userPermissionEntity.getId().getPermissionId())")
    @Mapping(target = "title", expression = "java(userPermissionEntity.getPermission().getTitle())")
    @Mapping(target = "description", expression = "java(userPermissionEntity.getPermission().getDescription())")
    PermissionOfUserDTO mapFromUserPermissionEntity(UserPermissionEntity userPermissionEntity);
}
