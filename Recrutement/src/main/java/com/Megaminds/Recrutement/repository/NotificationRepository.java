package com.Megaminds.Recrutement.repository;

import com.Megaminds.Recrutement.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByReadFalse(); // Récupère les notifications non lues
}