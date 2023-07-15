package com.example.lmsbackend.dto.exam;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class QuizStateDto {

    @Pattern(regexp = "^(PUBLIC|PRIVATE)$",
            message = "For the quiz state, only values PUBLIC and PRIVATE are accepted")
    private String state;
}
