package com.example.lmsbackend.dto.exam.multi_choice;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class MultiChoiceOptionDto {
    private Long id;
    private String content;
    private Integer answerKey;
    private Boolean isCorrect;
    private Integer order;
    private Boolean isChosenByStudent = false;
}
