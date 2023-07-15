package com.example.lmsbackend.dto.exam.multi_choice;

import com.example.lmsbackend.dto.exam.QuestionDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class MultiChoicesQuestionDto extends QuestionDto {
    private Boolean isMultipleAnswer;
    private List<MultiChoiceOptionDto> answerList;
}
