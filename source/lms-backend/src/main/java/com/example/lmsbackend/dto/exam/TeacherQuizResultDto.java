package com.example.lmsbackend.dto.exam;

import lombok.Data;

@Data
public class TeacherQuizResultDto {
    private Integer totalParticipation = 0;
    private Double averageScore = 0.0;
    private Integer totalNumberOfPassedStudent = 0;
    private SessionResultPagedDto sessionsResult;
}
