package com.example.lmsbackend.mapper;

import com.example.lmsbackend.domain.UserRoleEntity;
import com.example.lmsbackend.dto.response.user.RoleOfUserDTO;
import com.example.lmsbackend.repository.RoleRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        componentModel = "spring"
)
public abstract class RoleOfUserDTOMapper {
    @Autowired
    protected RoleRepository roleRepository;

    @Mapping(target = "id", expression = "java(userRoleEntity.getId().getRoleId())")
    @Mapping(target = "title", expression = "java(getRoleTitle(userRoleEntity))")
    public abstract RoleOfUserDTO mapFromUserRoleEntity(UserRoleEntity userRoleEntity);

    protected String getRoleTitle(UserRoleEntity userRoleEntity){
        return roleRepository.getById(userRoleEntity.getId().getRoleId()).getTitle();
    }
}
