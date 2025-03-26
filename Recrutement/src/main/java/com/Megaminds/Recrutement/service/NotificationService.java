package com.Megaminds.Recrutement.service;

import com.Megaminds.Recrutement.dto.NotificationDTO;
import com.Megaminds.Recrutement.entity.Notification;
import com.Megaminds.Recrutement.entity.Application;
import com.Megaminds.Recrutement.entity.Interview;
import com.Megaminds.Recrutement.repository.NotificationRepository;
import com.Megaminds.Recrutement.repository.ApplicationRepository;
import com.Megaminds.Recrutement.repository.InterviewRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final ApplicationRepository applicationRepository;
    private final InterviewRepository interviewRepository;

    public NotificationService(NotificationRepository notificationRepository,
                               ApplicationRepository applicationRepository,
                               InterviewRepository interviewRepository) {
        this.notificationRepository = notificationRepository;
        this.applicationRepository = applicationRepository;
        this.interviewRepository = interviewRepository;
    }

    // Create a notification for the candidate
    public void createNotification(String type, String message, Long applicationId, Long interviewId) {
        try {
            // Fetch the application
            Application application = applicationRepository.findById(applicationId)
                    .orElseThrow(() -> new RuntimeException("Application not found"));

            // Create a new notification
            Notification notification = new Notification();
            notification.setType(type);
            notification.setMessage(message);
            notification.setRead(false); // Mark as unread by default
            notification.setCreatedAt(LocalDateTime.now()); // Set creation time
            notification.setApplication(application); // Link to the application

            // Link the interview if provided
            if (interviewId != null) {
                Interview interview = interviewRepository.findById(interviewId)
                        .orElseThrow(() -> new RuntimeException("Interview not found"));
                notification.setInterview(interview);
            }

            // Save the notification
            notificationRepository.save(notification);
        } catch (Exception e) {
            System.err.println("Error creating notification: " + e.getMessage());
            throw e; // Re-throw the exception for the controller to handle
        }
    }

    // Fetch all notifications
    public List<NotificationDTO> getNotifications() {
        return notificationRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Mark a notification as read
    public void markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    // Convert Notification entity to NotificationDTO
    private NotificationDTO convertToDTO(Notification notification) {
        NotificationDTO dto = new NotificationDTO();
        dto.setType(notification.getType());
        dto.setMessage(notification.getMessage());
        dto.setApplicationId(notification.getApplication().getId());
        if (notification.getInterview() != null) {
            dto.setInterviewId(notification.getInterview().getId());
        }
        dto.setRead(notification.isRead());
        return dto;
    }
}