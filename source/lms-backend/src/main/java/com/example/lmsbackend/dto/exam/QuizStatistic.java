package com.example.lmsbackend.dto.exam;

import lombok.Data;

import java.util.List;

@Data
public class QuizStatistic {
    private List<QuestionStatistic> questions;
    private Double averageScore;
    private Double averageAttempt;
    private Double averageTimeTaken;
    private String courseName;
    private String courseCode;
    private String className;
    private String classCode;
}
