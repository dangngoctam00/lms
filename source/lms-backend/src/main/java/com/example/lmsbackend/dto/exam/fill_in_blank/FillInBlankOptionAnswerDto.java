package com.example.lmsbackend.dto.exam.fill_in_blank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FillInBlankOptionAnswerDto extends FillInBlankOptionDto {
    private String studentAnswer;
    private Boolean isCorrect;
}
