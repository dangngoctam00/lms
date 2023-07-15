package com.example.lmsbackend.exceptions.aclass;

public class MemberNotFoundInClassException extends RuntimeException {
    private Long memberId;
    private Long classId;

    public MemberNotFoundInClassException(Long classId, Long memberId) {
        this.classId = classId;
        this.memberId = memberId;
    }

    @Override
    public String getMessage() {
        return String.format("User %s is not found in class %s", memberId, classId);
    }
}
