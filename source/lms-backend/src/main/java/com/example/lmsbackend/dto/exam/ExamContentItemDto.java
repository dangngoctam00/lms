package com.example.lmsbackend.dto.exam;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ExamContentItemDto extends CreateExamRequestDto {
    private Long id;
    private String courseName;
    private LocalDateTime updatedAt;
}
