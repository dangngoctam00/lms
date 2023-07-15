package com.example.lmsbackend.exceptions.aclass;

import lombok.Getter;

@Getter
public class StudentNotFoundInClassException extends RuntimeException {
    private Long classId;
    private Long studentId;

    public StudentNotFoundInClassException(Long studentId, Long classId) {
        this.classId = classId;
        this.studentId = studentId;
    }

    @Override
    public String getMessage() {
        return String.format("Student %s is not found in class %s", studentId, classId);
    }
}
