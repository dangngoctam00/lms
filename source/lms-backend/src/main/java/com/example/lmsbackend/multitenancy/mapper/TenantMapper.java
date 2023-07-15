package com.example.lmsbackend.multitenancy.mapper;

import com.example.lmsbackend.multitenancy.domain.TenantEntity;
import com.example.lmsbackend.multitenancy.dto.TenantDto;
import com.example.lmsbackend.multitenancy.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TenantMapper {

    @Mapping(target = "tenantId", source = "domain")
    @Mapping(target = "schema", source = "domain")
    TenantEntity mapToTenant(TenantDto dto);


    @Mapping(target = "firstName", source = "firstname")
    @Mapping(target = "lastName", source = "lastname")
    UserDto mapToUserDto(TenantEntity tenantEntity);
}
