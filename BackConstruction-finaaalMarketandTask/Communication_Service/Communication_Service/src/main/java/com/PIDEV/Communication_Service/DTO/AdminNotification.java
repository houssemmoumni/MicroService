package com.PIDEV.Communication_Service.DTO;

public class AdminNotification {
    private String message;
    private int pendingCount;

    public AdminNotification(String message, int pendingCount) {
        this.message = message;
        this.pendingCount = pendingCount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getPendingCount() {
        return pendingCount;
    }

    public void setPendingCount(int pendingCount) {
        this.pendingCount = pendingCount;
    }
}
