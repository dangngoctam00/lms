package com.example.lmsbackend.dto.classes;

import lombok.Data;

@Data
public class StudentAttendanceDto {

    private String name;
    private Double attendanceRate;
    private String avatar;
    private String state = "NONE";
}
