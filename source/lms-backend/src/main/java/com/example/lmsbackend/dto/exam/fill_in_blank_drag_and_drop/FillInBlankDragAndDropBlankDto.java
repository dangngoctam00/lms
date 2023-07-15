package com.example.lmsbackend.dto.exam.fill_in_blank_drag_and_drop;

import lombok.Data;

@Data
public class FillInBlankDragAndDropBlankDto {
    private Long id;
    private String hint;
    private Integer answerKey;
    private String studentAnswer;
    private Integer order;
}
