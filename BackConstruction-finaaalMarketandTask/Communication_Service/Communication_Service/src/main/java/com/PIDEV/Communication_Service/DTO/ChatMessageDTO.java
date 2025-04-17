package com.PIDEV.Communication_Service.DTO;

public class ChatMessageDTO {
    private String message;
    private String response;

    // Getters et setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}