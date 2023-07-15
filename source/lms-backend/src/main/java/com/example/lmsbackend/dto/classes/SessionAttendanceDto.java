package com.example.lmsbackend.dto.classes;

import lombok.Data;

import java.util.Map;

@Data
public class SessionAttendanceDto {
    private AttendanceMetaData metaInfo;
    private Map<Long, StudentAttendanceDto> studentAttendance;
}
