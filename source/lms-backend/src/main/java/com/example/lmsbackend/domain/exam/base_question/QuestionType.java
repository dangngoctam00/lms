package com.example.lmsbackend.domain.exam.base_question;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QuestionType {
    MULTI_CHOICES("MULTI_CHOICE"),
    WRITING("WRITING"),
    FILL_IN_BLANK_WITH_CHOICES("FILL_IN_BLANK_WITH_CHOICES"),
    SUBMIT_FILE("SUBMIT_FILE"),
    FILL_IN_BLANK_DRAG_AND_DROP("FILL_IN_BLANK_DRAG_AND_DROP"),
    GROUP("GROUP"),
    FILL_IN_BLANK("FILL_IN_BLANK");

    final String type;
}