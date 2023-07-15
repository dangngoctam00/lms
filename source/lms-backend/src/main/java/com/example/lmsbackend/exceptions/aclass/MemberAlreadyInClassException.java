package com.example.lmsbackend.exceptions.aclass;

public class MemberAlreadyInClassException extends RuntimeException {
    private Long memberId;
    private Long classId;

    public MemberAlreadyInClassException(Long classId, Long memberId) {
        this.classId = classId;
        this.memberId = memberId;
    }
    
    @Override
    public String getMessage() {
        return String.format("User %s is already in class %s", memberId, classId);
    }
}
