package com.example.lmsbackend.dto.classes;

import lombok.Data;

@Data
public class AttendanceStatistics {
    private Double averageAttendance = 0d;
    private Integer numOfStudentWithPerfectAttendance = 0;
    private Integer numOfStudentWithAboveAverageAttendance = 0;
    private Integer numOfStudentWithBelowAverageAttendance = 0;
}