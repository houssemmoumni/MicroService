// src/main/java/com/megaminds/incident/repository/NotificationRepository.java
package com.megaminds.incident.repository;

import com.megaminds.incident.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByReceiverId(Long userId);
}