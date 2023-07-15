package com.example.lmsbackend.dto.exam.multi_choice;

import com.example.lmsbackend.dto.exam.QuestionAnswerDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MultiChoiceQuestionAnswerDto extends QuestionAnswerDto {
    private Boolean isMultipleAnswer;
    private List<MultiChoiceOptionAnswerDto> resultAnswerList;
}
