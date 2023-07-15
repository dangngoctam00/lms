package com.example.lmsbackend.exceptions.exam;

public class ExamNotFoundException extends RuntimeException {

    private Long examId;

    public ExamNotFoundException(Long examId) {
        this.examId = examId;
    }

    @Override
    public String getMessage() {
        return String.format("Exam: %s is not found", examId);
    }
}
