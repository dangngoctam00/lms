package com.example.lmsbackend.dto.exam;

import com.example.lmsbackend.enums.QuestionAnswerStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionInExamByTypeAndIdDto extends QuestionByTypeDto {
    private Integer order;
    private QuestionAnswerStatus status;
    private Boolean flagged = false;

    public QuestionInExamByTypeAndIdDto(Long id, String type, Integer order) {
        super(id, type);
        this.order = order;
    }
}
