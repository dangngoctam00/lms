package com.example.lmsbackend.dto.exam.writing;

import com.example.lmsbackend.dto.exam.QuestionDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class WritingQuestionDto extends QuestionDto {

    private String studentAnswer = "";
}
