package com.example.lmsbackend.enums;

import lombok.Getter;

@Getter
public enum ExamState {
    PRIVATE("PRIVATE"),
    PUBLIC("PUBLIC");

    final String state;

    ExamState(String state) {
        this.state = state;
    }
}
