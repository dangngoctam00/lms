package com.example.lmsbackend.dto.exam.fill_in_blank_multi_choices;

import com.example.lmsbackend.dto.exam.BlankDto;
import lombok.Data;

import java.util.List;

@Data
public class FillInBlankMultiChoicesBlankDto extends BlankDto {
    private Integer correctAnswerKey;
    private List<FillInBlankMultiChoicesOptionDto> answerList;
    private Integer studentAnswer;
    private Integer order;
}
