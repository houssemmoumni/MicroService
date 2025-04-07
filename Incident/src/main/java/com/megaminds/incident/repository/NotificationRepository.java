package com.megaminds.incident.repository;

import com.megaminds.incident.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByReceiverId(Long receiverId);
    List<Notification> findByReceiverIdAndIsReadFalse(Long receiverId);

    @Transactional
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.receiverId = :receiverId AND n.isRead = false")
    void markAllAsRead(Long receiverId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Notification n WHERE n.receiverId = :receiverId")
    void deleteByReceiverId(Long receiverId);
}