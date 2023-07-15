package com.example.lmsbackend.dto.classes;

import lombok.Data;

@Data
public class GradeTagStudentCount {
    private Long tagId;
    private Long count;

    public GradeTagStudentCount(Long tagId, Long count) {
        this.tagId = tagId;
        this.count = count;
    }
}
