package com.example.lmsbackend.dto.exam;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AnswerSessionDto {
    private List<String> answers;
    private String optionalStudentNote;
}
