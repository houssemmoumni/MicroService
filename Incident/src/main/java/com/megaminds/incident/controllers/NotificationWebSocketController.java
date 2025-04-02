package com.megaminds.incident.controllers;

import com.megaminds.incident.dto.IncidentNotificationDto;
import com.megaminds.incident.entity.Notification;
import com.megaminds.incident.service.NotificationService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
@CrossOrigin(origins = "http://localhost:4200")

@RestController
public class NotificationWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationService notificationService;

    public NotificationWebSocketController(
            SimpMessagingTemplate messagingTemplate,
            NotificationService notificationService) {
        this.messagingTemplate = messagingTemplate;
        this.notificationService = notificationService;
    }

    @MessageMapping("/incident")
    public void handleNewIncident(IncidentNotificationDto incident) {
        Notification notification = new Notification();
        notification.setMessage(String.format(
                "New %s severity incident reported by %s: %s",
                incident.getSeverity(),
                incident.getReporterName(),
                incident.getDescription()
        ));
        notification.setSeverity(incident.getSeverity());
        notification.setNotificationDate(LocalDateTime.now());
        notification.setReceiverId(1L); // Admin ID

        Notification savedNotification = notificationService.createNotification(notification);
        messagingTemplate.convertAndSend("/topic/notifications", savedNotification);
    }
}