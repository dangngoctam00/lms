package com.example.lmsbackend.mapper;

import com.example.lmsbackend.domain.StaffEntity;
import com.example.lmsbackend.dto.staff.StaffDTO;
import com.example.lmsbackend.dto.staff.StaffSimple;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StaffMapper {
    StaffMapper INSTANCE = Mappers.getMapper(StaffMapper.class);

    StaffDTO mapFromStaffEntityToStaffDTO(StaffEntity entity);

    StaffSimple mapFromStaffEntityToStaffSimple(StaffEntity entity);
}
