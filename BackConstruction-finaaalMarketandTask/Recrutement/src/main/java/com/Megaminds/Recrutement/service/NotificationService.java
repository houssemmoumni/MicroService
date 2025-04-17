package com.Megaminds.Recrutement.service;

import com.Megaminds.Recrutement.dto.NotificationDTO;
import com.Megaminds.Recrutement.entity.*;
import com.Megaminds.Recrutement.repository.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final ApplicationRepository applicationRepository;
    private final InterviewRepository interviewRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(NotificationRepository notificationRepository,
                               ApplicationRepository applicationRepository,
                               InterviewRepository interviewRepository,
                               SimpMessagingTemplate messagingTemplate) {
        this.notificationRepository = notificationRepository;
        this.applicationRepository = applicationRepository;
        this.interviewRepository = interviewRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public void createNotification(String type, String message, Long applicationId, Long interviewId) {
        try {
            Application application = applicationRepository.findById(applicationId)
                    .orElseThrow(() -> new RuntimeException("Application not found"));

            Notification notification = new Notification();
            notification.setType(type);
            notification.setMessage(message);
            notification.setRead(false);
            notification.setCreatedAt(LocalDateTime.now());
            notification.setApplication(application);

            if (interviewId != null) {
                Interview interview = interviewRepository.findById(interviewId)
                        .orElseThrow(() -> new RuntimeException("Interview not found"));
                notification.setInterview(interview);
            }

            Notification savedNotification = notificationRepository.save(notification);

            // Envoyer la notification via WebSocket
            NotificationDTO notificationDTO = convertToDTO(savedNotification);
            messagingTemplate.convertAndSend("/topic/notifications", notificationDTO);

        } catch (Exception e) {
            System.err.println("Error creating notification: " + e.getMessage());
            throw e;
        }
    }

    public List<NotificationDTO> getNotifications() {
        return notificationRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private NotificationDTO convertToDTO(Notification notification) {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(notification.getId());
        dto.setType(notification.getType());
        dto.setMessage(notification.getMessage());
        dto.setApplicationId(notification.getApplication().getId());
        if (notification.getInterview() != null) {
            dto.setInterviewId(notification.getInterview().getId());
        }
        dto.setRead(notification.isRead());
        dto.setCreatedAt(notification.getCreatedAt().toString());
        return dto;
    }

    public void markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }
}