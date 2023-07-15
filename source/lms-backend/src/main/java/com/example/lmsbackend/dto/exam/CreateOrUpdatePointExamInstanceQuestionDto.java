package com.example.lmsbackend.dto.exam;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class CreateOrUpdatePointExamInstanceQuestionDto {

    @NotNull(message = "This is a required field")
    private Integer point;

    @NotNull(message = "This is a required field")
    @Pattern(regexp = "^(MULTI_CHOICE|WRITING|FILL_IN_BLANK_WITH_CHOICES|SUBMIT_FILE|FILL_IN_BLANK_DRAG_AND_DROP|FILL_IN_BLANK|GROUP)$",
            message = "For this type of question, only values: MULTI_CHOICE, WRITING, FILL_IN_BLANK_WITH_CHOICES, SUBMIT_FILE, FILL_IN_BLANK_DRAG_AND_DROP, FILL_IN_BLANK, GROUP are accepted")
    private String type;
}
