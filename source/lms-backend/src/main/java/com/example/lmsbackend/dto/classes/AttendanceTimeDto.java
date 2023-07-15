package com.example.lmsbackend.dto.classes;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class AttendanceTimeDto {
    private AttendanceTimeMetaData metaInfo;
    private Map<Long, StudentAttendanceDto> studentAttendance = new HashMap<>();
}
