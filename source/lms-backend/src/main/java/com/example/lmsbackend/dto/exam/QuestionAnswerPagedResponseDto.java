package com.example.lmsbackend.dto.exam;

import com.example.lmsbackend.dto.response.AbstractPagedDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class QuestionAnswerPagedResponseDto extends AbstractPagedDto {
    private List<QuestionAnswerDto> listData = new ArrayList<>();
    private List<UngradedQuestionDto> unGradedQuestions = new ArrayList<>();
}
