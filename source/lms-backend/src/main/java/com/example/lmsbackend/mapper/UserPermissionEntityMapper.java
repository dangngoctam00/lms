package com.example.lmsbackend.mapper;

import com.example.lmsbackend.domain.UserPermissionEntity;
import com.example.lmsbackend.dto.response.auth.GrantedPermission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserPermissionEntityMapper {
    UserPermissionEntityMapper INSTANCE = Mappers.getMapper(UserPermissionEntityMapper.class);

    default UserPermissionEntity mapFromUserIdAndGrantedPermission(Long userId, GrantedPermission grantedPermission){
        UserPermissionEntity userPermissionEntity = mapFromGrantedPermission(grantedPermission);
        userPermissionEntity.getId().setUserId(userId);
        userPermissionEntity.getId().setPermissionId(grantedPermission.getId());

        return userPermissionEntity;
    }

    @Mapping(target = "id", ignore = true)
    UserPermissionEntity mapFromGrantedPermission(GrantedPermission grantedPermission);
}
