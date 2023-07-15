package com.example.lmsbackend.dto.exam;

import com.example.lmsbackend.enums.ExamState;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CreateExamRequestDto {

    @NotBlank(message = "This is a required field")
    private String title;

    @NotBlank(message = "This is a required field")
    private String description;

    private ExamState state;

    private List<TextbookExamDto> textbooks = new ArrayList<>();
}
