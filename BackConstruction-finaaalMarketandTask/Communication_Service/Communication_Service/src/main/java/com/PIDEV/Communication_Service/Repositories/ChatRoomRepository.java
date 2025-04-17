package com.PIDEV.Communication_Service.Repositories;

import com.PIDEV.Communication_Service.Entities.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    // Trouver une salle de chat par son nom
    Optional<ChatRoom> findByName(String name);

    // Trouver les salles d'un utilisateur
    @Query("SELECT cr FROM ChatRoom cr JOIN cr.participants p WHERE p.Iduser = :userId")
    List<ChatRoom> findByParticipantId(@Param("userId") Long userId);
}