package com.example.lmsbackend.exceptions.aclass;

public class GradeTagNotFoundException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Tag not found";
    }
}
