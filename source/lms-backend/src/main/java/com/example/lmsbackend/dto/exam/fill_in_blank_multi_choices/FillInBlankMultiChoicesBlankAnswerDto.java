package com.example.lmsbackend.dto.exam.fill_in_blank_multi_choices;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FillInBlankMultiChoicesBlankAnswerDto extends FillInBlankMultiChoicesBlankDto {
    private Integer studentAnswerKey;
    private Boolean isCorrect;
}
