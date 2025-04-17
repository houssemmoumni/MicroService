package com.PIDEV.Communication_Service.Services;

import com.PIDEV.Communication_Service.Entities.ChatRoom;
import com.PIDEV.Communication_Service.Entities.Message;
import com.PIDEV.Communication_Service.Entities.User;
import com.PIDEV.Communication_Service.Repositories.MessageRepository;
import com.PIDEV.Communication_Service.Repositories.ChatRoomRepository;
import com.PIDEV.Communication_Service.Repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService implements IMessageService {

    @Autowired
    MessageRepository messageRepository;
    @Autowired ChatRoomRepository chatRoomRepository;
    @Autowired UserRepository userRepository;

    @Override
    @Transactional
    public Message sendMessage(Long chatRoomId, Long senderId, String content) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("ChatRoom not found"));

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Message message = new Message();
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());
        message.setSender(sender);
        message.setSenderId(sender.getIduser()); // Assurez-vous que ce champ est bien rempli
        message.setChatRoom(chatRoom);

        Message savedMessage = messageRepository.save(message);

        // Mise à jour bidirectionnelle si nécessaire
        chatRoom.getMessages().add(savedMessage);
        chatRoomRepository.save(chatRoom);

        return savedMessage;
    }

    @Override
    public List<Message> getMessagesByChatRoom(Long chatRoomId) {
        return messageRepository.findByChatRoom_IdOrderByTimestampAsc(chatRoomId);
    }

    // Nouvelle méthode pour la compatibilité frontend
    public List<Message> getMessagesForFrontend(Long chatRoomId) {
        return getMessagesByChatRoom(chatRoomId).stream()
                .peek(message -> {
                    // Force le chargement lazy si nécessaire
                    if (message.getSender() != null) {
                        message.setSenderId(message.getSender().getIduser());
                    }
                })
                .collect(Collectors.toList());
    }
}