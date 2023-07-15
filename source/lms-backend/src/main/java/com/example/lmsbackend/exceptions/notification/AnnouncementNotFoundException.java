package com.example.lmsbackend.exceptions.notification;

public class AnnouncementNotFoundException extends RuntimeException {
    private Long id;

    public AnnouncementNotFoundException(Long id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        String messageTemplate = "Announcement '%s' is not found";
        return String.format(messageTemplate, id);
    }
}
