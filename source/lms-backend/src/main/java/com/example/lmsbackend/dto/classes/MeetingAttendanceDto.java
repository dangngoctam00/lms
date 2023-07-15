package com.example.lmsbackend.dto.classes;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class MeetingAttendanceDto {
    private AttendanceMetaData metaInfo;
    private Map<String, AttendanceTimeDto> attendanceInfo = new HashMap<>();
}
