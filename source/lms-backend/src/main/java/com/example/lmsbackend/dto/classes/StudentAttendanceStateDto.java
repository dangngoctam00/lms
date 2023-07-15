package com.example.lmsbackend.dto.classes;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class StudentAttendanceStateDto {

    @Pattern(regexp = "^(PRESENT|ABSENT|LATE|NONE)$",
            message = "For the state, only values PRESENT, ABSENT, LATE, NONE are accepted")
    private String state;
}