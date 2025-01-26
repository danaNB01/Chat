package com.trendyol.chat.config;

import com.trendyol.chat.chat.MessageType;
import com.trendyol.chat.chat.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@Slf4j
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messageTemplate;
    // Explicit constructor for injection
    public WebSocketEventListener(SimpMessageSendingOperations messageTemplate) {
        this.messageTemplate = messageTemplate;
    }


    // SessionDisconnectEvent is triggered by spring when web socket connection is terminated.
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        // access header and session attributes of the web socket msg.
        // wrap extract info.
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if (username != null) {
            System.out.println("User disconnected: " + username);
            ChatMessage chatMessage = ChatMessage.builder()
                    .type(MessageType.LEAVE)
                    .sender(username)
                    .build();

            // broadcast the chatMessage to all clients subscriped to /topic/public.
            messageTemplate.convertAndSend("/topic/public", chatMessage);
        }

    }
}
