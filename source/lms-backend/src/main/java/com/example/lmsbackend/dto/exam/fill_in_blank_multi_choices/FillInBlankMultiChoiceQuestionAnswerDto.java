package com.example.lmsbackend.dto.exam.fill_in_blank_multi_choices;

import com.example.lmsbackend.dto.exam.QuestionAnswerDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FillInBlankMultiChoiceQuestionAnswerDto extends QuestionAnswerDto {
    private List<FillInBlankMultiChoicesBlankAnswerDto> resultAnswerList;
}
