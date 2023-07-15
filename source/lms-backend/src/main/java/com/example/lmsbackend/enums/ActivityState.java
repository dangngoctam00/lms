package com.example.lmsbackend.enums;

import lombok.Getter;

@Getter
public enum ActivityState {
    PRIVATE("PRIVATE"),
    PUBLIC("PUBLIC");

    final String state;

    ActivityState(String state) {
        this.state = state;
    }
}
