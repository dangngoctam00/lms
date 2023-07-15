package com.example.lmsbackend.dto.classes;

import lombok.Data;

import java.util.Map;

@Data
public class OverallAttendanceDto {
    private Map<String, SessionAttendanceDto> attendanceInfo;
    private AttendanceStatistics statistics;
    private Map<Long, Integer> presentCount;
    private Integer happenedSession = 0;
}
