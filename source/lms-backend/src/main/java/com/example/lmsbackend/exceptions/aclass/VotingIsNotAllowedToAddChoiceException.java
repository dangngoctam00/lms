package com.example.lmsbackend.exceptions.aclass;

public class VotingIsNotAllowedToAddChoiceException extends RuntimeException {
    private Long votingId;

    public VotingIsNotAllowedToAddChoiceException(Long votingId) {
        this.votingId = votingId;
    }

    @Override
    public String getMessage() {
        return String.format("Student is not allowed to add choices to voting %s", votingId);
    }
}
