package com.example.lmsbackend.dto.exam;

import com.example.lmsbackend.dto.response.user.UsersPagedDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class QuizEntryDto {
    private Long id;
    private String title;
    private String description;
    private Long courseId;
    private String courseName;
    private String state;
    private Integer totalGrade;

    private QuizConfigDto config;
    private QuestionPagedResponseDto questionList;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UsersPagedDto.UserInformation createdBy;
    private UsersPagedDto.UserInformation updatedBy;
}
