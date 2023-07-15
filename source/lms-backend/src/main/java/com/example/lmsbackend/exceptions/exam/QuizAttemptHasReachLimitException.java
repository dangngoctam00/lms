package com.example.lmsbackend.exceptions.exam;

public class QuizAttemptHasReachLimitException extends RuntimeException {
    private Long studentId;
    private Long quizId;

    public QuizAttemptHasReachLimitException(Long studentId, Long quizId) {
        this.studentId = studentId;
        this.quizId = quizId;
    }

    @Override
    public String getMessage() {
        return String.format("Student %s has reach the limit attempt of quiz %s", studentId, quizId);
    }
}
