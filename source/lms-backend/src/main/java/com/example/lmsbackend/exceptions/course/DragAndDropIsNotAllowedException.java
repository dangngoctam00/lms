package com.example.lmsbackend.exceptions.course;

public class DragAndDropIsNotAllowedException extends RuntimeException {
    public DragAndDropIsNotAllowedException() {

    }

    @Override
    public String getMessage() {
        return "Drag and drop is not allowed because there are active class which belong to this course";
    }
}
