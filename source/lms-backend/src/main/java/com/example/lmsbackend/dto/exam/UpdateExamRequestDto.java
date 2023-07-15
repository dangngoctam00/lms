package com.example.lmsbackend.dto.exam;

import com.example.lmsbackend.enums.ExamState;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UpdateExamRequestDto {

    @NotNull(message = "This is a required field")
    private Long id;

    @NotBlank(message = "This is a required field")
    private String title;

    @NotBlank(message = "This is a required field")
    private String description;

    @NotNull(message = "This is a required field")
    private Long courseId;

    private ExamState state;
}
