package com.PIDEV.Communication_Service.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Builder
@Data
public class WebSocketMessageDTO {
   Long senderId;
     Long chatRoomId;  // Must match the getter name
   String content;
     LocalDateTime timestamp;
    String senderName; // Ajout recommandé
     String senderAvatar; // Ajout recommandé

    public WebSocketMessageDTO() {

    }


    // Manual getters
    public Long getSenderId() {
        return senderId;
    }

    public Long getChatRoomId() {
        return chatRoomId;
    }

    public String getContent() {
        return content;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public void setChatRoomId(Long chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderAvatar() {
        return senderAvatar;
    }
    public void setSenderAvatar(String senderAvatar) {
        this.senderAvatar = senderAvatar;
    }



    public WebSocketMessageDTO(Long chatRoomId, Long senderId, String content, LocalDateTime timestamp, String senderName, String senderAvatar) {
        // initialisation des champs
    }
}
