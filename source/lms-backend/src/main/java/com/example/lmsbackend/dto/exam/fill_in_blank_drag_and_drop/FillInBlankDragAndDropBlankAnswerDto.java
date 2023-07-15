package com.example.lmsbackend.dto.exam.fill_in_blank_drag_and_drop;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FillInBlankDragAndDropBlankAnswerDto extends FillInBlankDragAndDropBlankDto {
    private Integer studentAnswerKey;
    private String studentAnswer;
    private String expectedAnswer;
    private Boolean isCorrect;
}
