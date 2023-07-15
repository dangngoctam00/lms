package com.example.lmsbackend.exceptions.exam;

public class UnsupportedMatchStrategyException extends RuntimeException {
    private String strategy;

    public UnsupportedMatchStrategyException(String strategy) {
        this.strategy = strategy;
    }

    @Override
    public String getMessage() {
        return String.format("Strategy %s is not supported", strategy);
    }
}
