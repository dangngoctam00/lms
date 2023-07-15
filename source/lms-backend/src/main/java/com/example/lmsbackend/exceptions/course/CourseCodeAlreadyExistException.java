package com.example.lmsbackend.exceptions.course;

public class CourseCodeAlreadyExistException extends RuntimeException {

    private final String code;
    private final String message = "Mã khóa học %s đã tồn tại";

    public CourseCodeAlreadyExistException(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return String.format(message, code);
    }
}
