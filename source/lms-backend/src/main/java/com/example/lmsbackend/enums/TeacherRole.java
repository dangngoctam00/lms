package com.example.lmsbackend.enums;

import lombok.Getter;

@Getter
public enum TeacherRole {
    TEACHER("TEACHER"),
    TEACHER_ASSISTANT("TEACHER_ASSISTANT");

    final String role;

    TeacherRole(String role) {
        this.role = role;
    }
}