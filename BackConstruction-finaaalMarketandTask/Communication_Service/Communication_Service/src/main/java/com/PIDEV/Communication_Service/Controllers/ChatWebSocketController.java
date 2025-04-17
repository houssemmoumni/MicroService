package com.PIDEV.Communication_Service.Controllers;

import com.PIDEV.Communication_Service.DTO.WebSocketMessageDTO;
import com.PIDEV.Communication_Service.Services.MessageService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.support.MessageBuilder;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    @Autowired SimpMessagingTemplate messagingTemplate;
    @Autowired MessageService messageService;

    @MessageMapping("/chat.send/{chatRoomId}")
    @SendTo("/topic/chatroom/{chatRoomId}")
    public WebSocketMessageDTO handleMessage(
            @Payload WebSocketMessageDTO messageDTO,
            @DestinationVariable Long chatRoomId,
            SimpMessageHeaderAccessor headerAccessor) {

        // Validation
        if (!chatRoomId.equals(messageDTO.getChatRoomId())) {
            throw new IllegalArgumentException("ChatRoom ID mismatch");
        }

        // 1. Sauvegarde du message (en utilisant le nom complet de votre classe Message)
        com.PIDEV.Communication_Service.Entities.Message savedMessage =
                messageService.sendMessage(
                        messageDTO.getChatRoomId(),
                        messageDTO.getSenderId(),
                        messageDTO.getContent()
                );

        // 2. Conversion en DTO
        return convertToWebSocketDTO(savedMessage);
    }

    // Méthode utilitaire pour la conversion
    private WebSocketMessageDTO convertToWebSocketDTO(com.PIDEV.Communication_Service.Entities.Message message) {
        if (message == null) {
            return null;
        }

        WebSocketMessageDTO dto = new WebSocketMessageDTO();
        dto.setChatRoomId(message.getChatRoom() != null ? message.getChatRoom().getId() : null);
        dto.setContent(message.getContent());
        dto.setSenderId(message.getSender() != null ? message.getSender().getIduser() : null);

        if (message.getSender() != null) {
            dto.setSenderName(message.getSender().getUsername()); // Supposant que User a getName()
            dto.setSenderAvatar(message.getSender().getAvatar()); // Supposant que User a getAvatar()
        }

        dto.setTimestamp(message.getTimestamp());

        return dto;
    }
    @MessageMapping("/chat.join/{chatRoomId}")
    public void handleUserJoin(
            @Payload WebSocketMessageDTO joinDTO,
            @DestinationVariable Long chatRoomId,
            SimpMessageHeaderAccessor headerAccessor) {

        headerAccessor.getSessionAttributes().put("userId", joinDTO.getSenderId());
        headerAccessor.getSessionAttributes().put("chatRoomId", chatRoomId);

        messagingTemplate.convertAndSend(
                "/topic/chatroom/" + chatRoomId,
                Map.of(
                        "type", "USER_JOINED",
                        "userId", joinDTO.getSenderId(),
                        "username", joinDTO.getSenderName(),
                        "timestamp", LocalDateTime.now()
                )
        );
    }
}