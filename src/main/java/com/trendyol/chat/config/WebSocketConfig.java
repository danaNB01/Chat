package com.trendyol.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // First, establish a connection to start communicating with a STOMP server.
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        // directly route msg to those destinations without server logic, no app code "@MessageMapping".
        // When a client sends a message to a destination like /topic/updates,
        // the broker immediately routes it to all clients subscribed to that topic.
        registry.enableSimpleBroker("/topic");

        // msg is routed to app-level method for processing.
        // Messages sent to /app/someAction are routed to @MessageMapping methods in my WebSocket controllers.
        registry.setApplicationDestinationPrefixes("/app");
    }
}
