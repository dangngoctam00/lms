package com.example.lmsbackend.exceptions.grade_formula;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class NotEnoughGradeException extends RuntimeException {
    private List<String> gradeTagNames;
}
