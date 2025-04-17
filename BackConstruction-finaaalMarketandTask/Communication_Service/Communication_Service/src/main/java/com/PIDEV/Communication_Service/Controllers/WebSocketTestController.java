package com.PIDEV.Communication_Service.Controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class WebSocketTestController {

    // Endpoint pour tester l'envoi de messages
    @MessageMapping("/send-test")
    @SendTo("/topic/test")
    public String sendTestMessage(String message) {
        return "Echo: " + message;
    }

    // Endpoint HTTP pour déclencher une notification
    @GetMapping("/trigger-notification")
    public void triggerNotification() {
        // Implémentation à compléter
    }
}