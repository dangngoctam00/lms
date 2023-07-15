package com.example.lmsbackend.exceptions.aclass;

public class StudentHasAlreadyInClassException extends RuntimeException {
    private Long classId;
    private Long studentId;

    public StudentHasAlreadyInClassException(Long classId, Long studentId) {
        this.classId = classId;
        this.studentId = studentId;
    }

    @Override
    public String getMessage() {
        return String.format("Student %s has already in belongs to class %s", studentId, classId);
    }
}
