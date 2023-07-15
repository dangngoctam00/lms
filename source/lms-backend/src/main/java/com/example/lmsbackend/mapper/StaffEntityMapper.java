package com.example.lmsbackend.mapper;

import com.example.lmsbackend.domain.StaffEntity;
import com.example.lmsbackend.dto.staff.StaffDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StaffEntityMapper {
    StaffEntityMapper INSTANCE = Mappers.getMapper(StaffEntityMapper.class);

    StaffEntity mapFromStaffDTO(StaffDTO staff);
}
