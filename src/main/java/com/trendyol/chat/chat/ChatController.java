package com.trendyol.chat.chat;


import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    // add a user
    // when a user connect to our chat app, when establishing a connection to the websocket
    // add the endpoint
    // inform the other users

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        // add the username of the user who established a connection, to the new created session attributes.
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }


    // send a msg
    // dispatch the msg that is sent by any user.

    // url to invoke this method.
    @MessageMapping("/chat.sendMessage")
    // after processing send it to 'broker-managed destination'
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage){
        return chatMessage;
    }

}
