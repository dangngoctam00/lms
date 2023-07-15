package com.example.lmsbackend.enums;

import lombok.Getter;

@Getter
public enum AttendanceState {
    NONE(0),
    PRESENT(1),
    LATE(2),
    ABSENT(3);

    final Integer priority;

    AttendanceState(Integer priority) {
        this.priority = priority;
    }
}
