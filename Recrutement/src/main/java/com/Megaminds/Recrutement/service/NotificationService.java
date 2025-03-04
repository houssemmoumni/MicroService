// src/main/java/com/Megaminds/Recrutement/service/NotificationService.java
package com.Megaminds.Recrutement.service;

import com.Megaminds.Recrutement.dto.NotificationDTO;
import com.Megaminds.Recrutement.entity.Notification;
import com.Megaminds.Recrutement.entity.Interview;
import com.Megaminds.Recrutement.repository.NotificationRepository;
import com.Megaminds.Recrutement.repository.InterviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final InterviewRepository interviewRepository;

    public NotificationService(NotificationRepository notificationRepository, InterviewRepository interviewRepository) {
        this.notificationRepository = notificationRepository;
        this.interviewRepository = interviewRepository;
    }

    // Récupère toutes les notifications non lues
    public List<NotificationDTO> getNotifications() {
        List<Notification> notifications = notificationRepository.findByReadFalse();
        return notifications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Convertit une entité Notification en DTO
    private NotificationDTO convertToDTO(Notification notification) {
        NotificationDTO dto = new NotificationDTO();
        dto.setType(notification.getType());
        dto.setMessage(notification.getMessage());
        dto.setInterviewId(notification.getInterview().getId());
        dto.setRead(notification.isRead());
        return dto;
    }

    // Crée une nouvelle notification
    public void createNotification(String type, String message, Long interviewId) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new RuntimeException("Interview not found"));

        Notification notification = new Notification();
        notification.setType(type);
        notification.setMessage(message);
        notification.setInterview(interview);
        notification.setRead(false);

        notificationRepository.save(notification);
    }

    // Marque une notification comme lue
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }
}