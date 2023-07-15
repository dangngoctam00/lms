package com.example.lmsbackend.dto.exam.writing;

import com.example.lmsbackend.dto.exam.QuestionAnswerDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WritingQuestionAnswerDto extends QuestionAnswerDto {
    private String answer;
}
