package com.example.lmsbackend.dto.grade_formula;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GradeTagDetailsDTO {
    private Long id;
    private String title;
    private boolean hasGraded;
    private LocalDateTime gradedAt;
    private LocalDateTime updatedAt;
    private boolean isPrimitive;
    private boolean isPublic;
}
