package com.megaminds.task.dto;

public class NotificationDTO {
    private String message;
    private int pendingCount;

    public NotificationDTO(String message, int pendingCount) {
        this.message = message;
        this.pendingCount = pendingCount;
    }

    public String getMessage() {
        return message;
    }

    public int getPendingCount() {
        return pendingCount;
    }
}
