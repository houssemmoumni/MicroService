package com.megaminds.finance.Controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationController {

    @MessageMapping("/notify") // client sends message here
    @SendTo("/topic/notifications") // message is broadcast here
    public String sendNotification(String message) {
        System.out.println("Received message: " + message);
        return message; // will be sent to all subscribers
    }
}
