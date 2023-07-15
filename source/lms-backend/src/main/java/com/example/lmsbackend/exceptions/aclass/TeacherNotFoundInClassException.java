package com.example.lmsbackend.exceptions.aclass;

import lombok.Getter;

@Getter
public class TeacherNotFoundInClassException extends RuntimeException {
    private Long classId;
    private Long teacherId;

    public TeacherNotFoundInClassException(Long teacherId, Long classId) {
        this.classId = classId;
        this.teacherId = teacherId;
    }

    @Override
    public String getMessage() {
        return String.format("Teacher %s is not found in class %s", teacherId, classId);
    }
}
