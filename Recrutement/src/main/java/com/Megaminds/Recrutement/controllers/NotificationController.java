package com.Megaminds.Recrutement.controllers;

import com.Megaminds.Recrutement.dto.NotificationDTO;
import com.Megaminds.Recrutement.service.NotificationService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:4200")
public class NotificationController {

    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationController(NotificationService notificationService,
                                  SimpMessagingTemplate messagingTemplate) {
        this.notificationService = notificationService;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getNotifications() {
        List<NotificationDTO> notifications = notificationService.getNotifications();
        return ResponseEntity.ok(notifications);
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/push-to-candidate")
    public void pushNotificationToCandidate(
            @RequestParam Long candidateId,
            @RequestBody NotificationDTO notificationDTO) {
        String destination = "/topic/candidate/notifications";
        messagingTemplate.convertAndSend(destination, notificationDTO);
    }
}