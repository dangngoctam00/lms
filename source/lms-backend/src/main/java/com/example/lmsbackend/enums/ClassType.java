package com.example.lmsbackend.enums;

import lombok.Getter;

@Getter
public enum ClassType {
    ONLINE("ONLINE"),
    OFFLINE("OFFLINE"),
    HYBRID("HYBRID");

    final String type;

    ClassType(String type) {
        this.type = type;
    }
}