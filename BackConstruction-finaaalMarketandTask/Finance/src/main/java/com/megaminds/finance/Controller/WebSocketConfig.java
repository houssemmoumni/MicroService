package com.megaminds.finance.Controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");  // where clients will subscribe
        config.setApplicationDestinationPrefixes("/app"); // where clients will send
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws") // this is the websocket connection URL
                .setAllowedOrigins("*") // use allowed origins for production
                .withSockJS(); // fallback for browsers not supporting websockets
    }
}
