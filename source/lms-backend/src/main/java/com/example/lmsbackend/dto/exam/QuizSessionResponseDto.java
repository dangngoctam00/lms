package com.example.lmsbackend.dto.exam;

import com.example.lmsbackend.dto.response.AbstractPagedDto;
import lombok.Data;

import java.util.List;

@Data
public class QuizSessionResponseDto extends AbstractPagedDto {

    private List<QuestionDto> listData;
    private Long remainingTime; // seconds
    private Boolean haveTimeLimit;
}
