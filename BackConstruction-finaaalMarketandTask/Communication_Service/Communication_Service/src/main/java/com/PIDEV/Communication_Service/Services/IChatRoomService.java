package com.PIDEV.Communication_Service.Services;

import com.PIDEV.Communication_Service.Entities.ChatRoom;
import java.util.List;
import java.util.Optional;

public interface IChatRoomService {
    ChatRoom createChatRoom(String name, List<Long> participantIds);
    Optional<ChatRoom> getChatRoomById(Long id);
    List<ChatRoom> getChatRoomsByUser(Long userId);
}
