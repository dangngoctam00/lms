package com.example.lmsbackend.mapper;

import com.example.lmsbackend.domain.UserRoleEntity;
import com.example.lmsbackend.dto.response.auth.GrantedRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserRoleEntityMapper {
    UserRoleEntityMapper INSTANCE = Mappers.getMapper(UserRoleEntityMapper.class);

    default UserRoleEntity mapFromUserIdAnfGrantedRole(Long userId, GrantedRole grantedRole){
        UserRoleEntity userRoleEntity = mapFromGrantedRole(grantedRole);
        userRoleEntity.getId().setUserId(userId);
        userRoleEntity.getId().setRoleId(grantedRole.getId());
        return userRoleEntity;
    }

    @Mapping(target = "id", ignore = true)
    UserRoleEntity mapFromGrantedRole(GrantedRole grantedRole);
}
