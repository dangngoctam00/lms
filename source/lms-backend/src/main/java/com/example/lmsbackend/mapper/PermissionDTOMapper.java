package com.example.lmsbackend.mapper;

import com.example.lmsbackend.domain.PermissionEntity;
import com.example.lmsbackend.dto.response.user.PermissionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PermissionDTOMapper {
    PermissionDTOMapper INSTANCE = Mappers.getMapper(PermissionDTOMapper.class);

    PermissionDTO mapFromPermissionEntity(PermissionEntity entity);
}
