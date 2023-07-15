package com.example.lmsbackend.multitenancy.mapper;

import com.example.lmsbackend.multitenancy.domain.PackageEntity;
import com.example.lmsbackend.multitenancy.dto.PackageDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PackageMapper {
    PackageDTO mapFromPackageEntity(PackageEntity entity);
}
