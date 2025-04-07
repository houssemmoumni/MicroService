package com.megaminds.incident.service;

import com.megaminds.incident.entity.Notification;
import com.megaminds.incident.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Notification createNotification(Notification notification) {
        notification.setRead(false); // Les nouvelles notifications sont non lues par défaut
        return notificationRepository.save(notification);
    }

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository.findByReceiverId(userId);
    }

    public List<Notification> getUnreadNotifications(Long receiverId) {
        return notificationRepository.findByReceiverIdAndIsReadFalse(receiverId);
    }

    public Optional<Notification> markAsRead(Long id) {
        return notificationRepository.findById(id)
                .map(notification -> {
                    notification.setRead(true);
                    return notificationRepository.save(notification);
                });
    }

    @Transactional
    public void markAllAsRead(Long receiverId) {
        notificationRepository.markAllAsRead(receiverId);
    }

    @Transactional
    public void clearAllNotifications(Long receiverId) {
        notificationRepository.deleteByReceiverId(receiverId);
    }
}
