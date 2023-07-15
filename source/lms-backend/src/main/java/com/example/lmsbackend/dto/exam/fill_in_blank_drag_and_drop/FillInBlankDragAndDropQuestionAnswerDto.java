package com.example.lmsbackend.dto.exam.fill_in_blank_drag_and_drop;

import com.example.lmsbackend.dto.exam.QuestionAnswerDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FillInBlankDragAndDropQuestionAnswerDto extends QuestionAnswerDto {
    private List<FillInBlankDragAndDropBlankAnswerDto> resultAnswerList;
    private List<FillInBlankDragAndDropAnswerDto> answerList;
}
