package com.example.lmsbackend.dto.exam;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class QuizSessionDto {
    private String id;
    private String instanceTitle;
    private String instanceDescription;
    private QuizConfigDto config;
    private QuestionPagedResponseDto questionList;
    private LocalDateTime timeStart;
    private LocalDateTime timeSubmitted;
}
