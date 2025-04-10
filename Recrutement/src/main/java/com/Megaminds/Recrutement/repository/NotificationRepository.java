package com.Megaminds.Recrutement.repository;

import com.Megaminds.Recrutement.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
