package com.megaminds.task.websocketConfig;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class MyWebSocketHandler extends TextWebSocketHandler {
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Handle incoming WebSocket messages
        System.out.println("Received message: " + message.getPayload());

        // You can send a message back to the client if needed
        session.sendMessage(new TextMessage("Message received"));
    }
}
