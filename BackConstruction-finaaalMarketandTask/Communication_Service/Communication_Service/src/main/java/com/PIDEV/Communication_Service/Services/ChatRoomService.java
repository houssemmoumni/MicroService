package com.PIDEV.Communication_Service.Services;

import com.PIDEV.Communication_Service.Entities.ChatRoom;
import com.PIDEV.Communication_Service.Entities.User;
import com.PIDEV.Communication_Service.Repositories.ChatRoomRepository;
import com.PIDEV.Communication_Service.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class ChatRoomService implements IChatRoomService {
    @Autowired ChatRoomRepository chatRoomRepository;
    @Autowired UserRepository userRepository;

    @Override
    public ChatRoom createChatRoom(String name, List<Long> participantIds) {
        List<User> participants = userRepository.findAllById(participantIds);

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setName(name);
        chatRoom.setParticipants(participants);

        return chatRoomRepository.save(chatRoom);
    }

    @Override
    public Optional<ChatRoom> getChatRoomById(Long id) {
        return chatRoomRepository.findById(id);
    }

    @Override
    public List<ChatRoom> getChatRoomsByUser(Long userId) {
        return chatRoomRepository.findByParticipantId(userId);
    }
}