package com.example.lmsbackend.dto.exam.multi_choice;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MultiChoiceOptionAnswerDto extends MultiChoiceOptionDto {
    private Boolean isChosenByStudent;
}
