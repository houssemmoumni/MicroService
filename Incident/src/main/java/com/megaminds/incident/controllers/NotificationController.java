package com.megaminds.incident.controllers;

import com.megaminds.incident.entity.Notification;
import com.megaminds.incident.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:4200")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getUserNotifications(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getUserNotifications(userId));
    }

    @GetMapping("/unread/{receiverId}")
    public ResponseEntity<List<Notification>> getUnreadNotifications(@PathVariable Long receiverId) {
        return ResponseEntity.ok(notificationService.getUnreadNotifications(receiverId));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<Notification> markAsRead(@PathVariable Long id) {
        Optional<Notification> notification = notificationService.markAsRead(id);
        return notification.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/read-all/{receiverId}")
    public ResponseEntity<Void> markAllAsRead(@PathVariable Long receiverId) {
        notificationService.markAllAsRead(receiverId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/clear-all/{receiverId}")
    public ResponseEntity<Void> clearAllNotifications(@PathVariable Long receiverId) {
        notificationService.clearAllNotifications(receiverId);
        return ResponseEntity.ok().build();
    }
}
