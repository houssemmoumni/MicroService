package com.Megaminds.Recrutement.service;

import com.Megaminds.Recrutement.dto.NotificationDTO;
import com.Megaminds.Recrutement.entity.*;
import com.Megaminds.Recrutement.repository.ApplicationRepository;
import com.Megaminds.Recrutement.repository.InterviewRepository;
import com.Megaminds.Recrutement.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final ApplicationRepository applicationRepository; // Add ApplicationRepository
    private final InterviewRepository interviewRepository; // Add InterviewRepository

    public NotificationService(NotificationRepository notificationRepository,
                               ApplicationRepository applicationRepository,
                               InterviewRepository interviewRepository) {
        this.notificationRepository = notificationRepository;
        this.applicationRepository = applicationRepository;
        this.interviewRepository = interviewRepository;
    }

    // Récupère toutes les notifications non lues
    public List<NotificationDTO> getNotifications() {
        List<Notification> notifications = notificationRepository.findByIsReadFalse(); // Fetch unread notifications
        return notifications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Convertit une entité Notification en DTO
    private NotificationDTO convertToDTO(Notification notification) {
        NotificationDTO dto = new NotificationDTO();
        dto.setType(notification.getType());
        dto.setMessage(notification.getMessage());
        dto.setRead(notification.isRead());
        if (notification.getApplication() != null) {
            dto.setApplicationId(notification.getApplication().getId()); // Lien vers l'application
        }
        if (notification.getInterview() != null) {
            dto.setInterviewId(notification.getInterview().getId()); // Lien vers l'entretien
        }
        return dto;
    }

    // Marque une notification comme lue
    public void markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    // Crée une nouvelle notification
    public void createNotification(String type, String message, Long applicationId, Long interviewId) {
        try {
            // Récupérer l'application associée
            Application application = applicationRepository.findById(applicationId)
                    .orElseThrow(() -> new RuntimeException("Application not found"));

            // Créer une nouvelle notification
            Notification notification = new Notification();
            notification.setType(type);
            notification.setMessage(message);
            notification.setRead(false); // Par défaut, la notification n'est pas lue
            notification.setCreatedAt(LocalDateTime.now()); // Date de création
            notification.setApplication(application); // Lier la notification à l'application

            // Lier l'entretien si fourni
            if (interviewId != null) {
                Interview interview = interviewRepository.findById(interviewId)
                        .orElseThrow(() -> new RuntimeException("Interview not found"));
                notification.setInterview(interview);
            }

            // Sauvegarder la notification
            notificationRepository.save(notification);
        } catch (Exception e) {
            System.err.println("Erreur lors de la création de la notification : " + e.getMessage());
            e.printStackTrace();
            throw e; // Relancez l'exception pour qu'elle soit capturée dans le contrôleur
        }
    }
}