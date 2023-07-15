package com.example.lmsbackend.dto.exam.fill_in_blank;

import com.example.lmsbackend.dto.exam.QuestionAnswerDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FillInBlankQuestionAnswerDto extends QuestionAnswerDto {
    private List<FillInBlankOptionAnswerDto> resultAnswerList;
}
