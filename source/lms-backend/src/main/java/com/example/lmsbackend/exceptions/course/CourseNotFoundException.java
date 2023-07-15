package com.example.lmsbackend.exceptions.course;

public class CourseNotFoundException extends RuntimeException {

    private final Long id;

    public CourseNotFoundException(Long id) {
        super();
        this.id = id;
    }

    @Override
    public String getMessage() {
        String messageTemplate = "Course %s not found";
        return String.format(messageTemplate, id);
    }

}

