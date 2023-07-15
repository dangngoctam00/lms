package com.example.lmsbackend.exceptions.aclass;

public class UserIsNotStudentOfClassException extends RuntimeException {
    private Long userId;
    private Long classId;

    public UserIsNotStudentOfClassException(Long userId, Long classId) {
        this.userId = userId;
        this.classId = classId;
    }

    @Override
    public String getMessage() {
        return String.format("User %s is not a student of class %s", userId, classId);
    }
}
