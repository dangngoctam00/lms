package com.example.lmsbackend.dto.exam.group;

import com.example.lmsbackend.dto.exam.QuestionAnswerDto;
import com.example.lmsbackend.enums.GradeQuestionState;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GroupQuestionAnswerDto extends QuestionAnswerDto {
    private List<QuestionAnswerDto> answerList;
    private GradeQuestionState resultState;
}
