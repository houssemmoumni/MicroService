package com.PIDEV.Communication_Service.Services;

import com.PIDEV.Communication_Service.Entities.Message;

import java.util.List;

public interface IMessageService {
    Message sendMessage(Long chatRoomId, Long senderId, String content);
    List<Message> getMessagesByChatRoom(Long chatRoomId);
}
