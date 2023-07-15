package com.example.lmsbackend.exceptions.aclass;

public class VotingNotFoundException extends RuntimeException {
    private Long voteId;

    public VotingNotFoundException(Long voteId) {
        this.voteId = voteId;
    }

    @Override
    public String getMessage() {
        return String.format("Voting %s is not found", voteId);
    }
}
