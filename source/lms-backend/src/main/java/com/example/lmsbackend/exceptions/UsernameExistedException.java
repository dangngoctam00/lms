package com.example.lmsbackend.exceptions;

public class UsernameExistedException extends RuntimeException {
    public UsernameExistedException(String username) {
        super(username + "is used");
    }
}
