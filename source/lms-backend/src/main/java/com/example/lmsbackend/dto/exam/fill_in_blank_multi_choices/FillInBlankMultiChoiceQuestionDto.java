package com.example.lmsbackend.dto.exam.fill_in_blank_multi_choices;

import com.example.lmsbackend.dto.exam.QuestionDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FillInBlankMultiChoiceQuestionDto extends QuestionDto {
    private List<FillInBlankMultiChoicesBlankDto> blankList;
}
