package com.example.lmsbackend.domain.event;

import lombok.Getter;

@Getter
public enum EventType {
    CLASS_SESSION,
    QUIZ,
    QUIZ_DEADLINE,
    QUIZ_OPEN
}