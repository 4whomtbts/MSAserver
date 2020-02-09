package com.zero.controller;

import com.zero.dto.ChattingMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChattingController {

    private SimpMessagingTemplate messagingTemplate;
    public static final String ENDPOINT_CHAT = "/chat";
    public static final String ENDPOINT_SUBSCRIBE_CHAT= "/sub" + ENDPOINT_CHAT + "/";
    public static final String ENDPOINT_PUBLISH_CHAT = "/pub" + ENDPOINT_CHAT;
    public static final String ENDPOINT_REGISTER = "/register";

    @Autowired
    public ChattingController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping(ENDPOINT_CHAT)
    public void chat(ChattingMessage message) {
        if(ChattingMessage.MessageType.ENTER.equals(message.getType())) {
            message.setMessage((message.getSender() + "님이 입장하셨습니다"));
        }
        messagingTemplate.convertAndSend(ENDPOINT_SUBSCRIBE_CHAT
                + message.getRoomId(), message);
    }
}
