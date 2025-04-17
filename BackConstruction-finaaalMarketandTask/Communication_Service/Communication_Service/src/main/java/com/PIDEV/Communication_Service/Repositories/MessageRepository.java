package com.PIDEV.Communication_Service.Repositories;

import com.PIDEV.Communication_Service.Entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    // Récupérer les messages d'une salle de chat
    List<Message> findByChatRoom_IdOrderByTimestampAsc(Long chatRoomId);
}