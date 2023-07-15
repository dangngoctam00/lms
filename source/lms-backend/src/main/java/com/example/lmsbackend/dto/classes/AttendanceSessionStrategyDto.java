package com.example.lmsbackend.dto.classes;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class AttendanceSessionStrategyDto {

    @Pattern(regexp = "^(ANY|LAST_TIME)$",
            message = "For the Strategy, only values ANY, LAST_TIME are accepted")
    private String strategy;
}
