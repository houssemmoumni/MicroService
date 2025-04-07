package com.megaminds.incident.service;

import com.megaminds.incident.entity.Notification;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendIncidentNotification(Notification notification) {
        messagingTemplate.convertAndSend("/topic/notifications", notification);
    }
}