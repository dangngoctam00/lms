package com.example.lmsbackend.dto.exam.fill_in_blank;

import com.example.lmsbackend.dto.exam.QuestionDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class FillInBlankQuestionDto extends QuestionDto {
    private List<FillInBlankOptionDto> blankList;
}
