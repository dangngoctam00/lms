package com.example.lmsbackend.exceptions.aclass;

public class VotingChoiceNotFoundException extends RuntimeException {
    private Long votingChoiceId;

    public VotingChoiceNotFoundException(Long votingChoiceId) {
        this.votingChoiceId = votingChoiceId;
    }

    @Override
    public String getMessage() {
        return String.format("Voting %s is not found", votingChoiceId);
    }
}
