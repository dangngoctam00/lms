package com.example.lmsbackend.dto.exam.group;

import com.example.lmsbackend.dto.exam.QuestionDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class GroupQuestionDto extends QuestionDto {
    private List<QuestionDto> questionList = new ArrayList<>();
}
