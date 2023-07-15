package com.example.lmsbackend.exceptions.notification;

public class UnAuthorizationUserToDeleteNotification extends RuntimeException {
    private Long userId;
    private Long notificationId;

    public UnAuthorizationUserToDeleteNotification(Long userId, Long notificationId) {
        this.userId = userId;
        this.notificationId = notificationId;
    }

    @Override
    public String getMessage() {
        return String.format("User %s doesn't have authorize to delete notification %s", userId, notificationId);
    }
}
