package com.example.lmsbackend.dto.exam.fill_in_blank_drag_and_drop;

import com.example.lmsbackend.dto.exam.QuestionDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FillInBlankDragAndDropQuestionDto extends QuestionDto {
    private List<FillInBlankDragAndDropAnswerDto> answerList = new ArrayList<>();
    private List<FillInBlankDragAndDropBlankDto> blankList = new ArrayList<>();
}
