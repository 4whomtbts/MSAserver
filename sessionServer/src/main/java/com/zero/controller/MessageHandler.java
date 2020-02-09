/*
package com.zero.controller;

import com.zero.dto.ChattingMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageHandler {
    @MessageMapping("/hello")
    @SendTo("/topic/roomId")
    public ChattingMessage broadcasting(ChattingMessage message) throws Exception {
        System.out.println("메세지 : " + message.getSender());
        return message;
    }
}

*/