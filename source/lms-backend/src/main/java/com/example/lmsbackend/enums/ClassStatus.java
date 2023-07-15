package com.example.lmsbackend.enums;

import lombok.Getter;

@Getter
public enum ClassStatus {
    CREATED("CREATED"),
    ONGOING("ONGOING"),
    ENDED("ENDED");

    final String type;

    ClassStatus(String type) {
        this.type = type;
    }
}
