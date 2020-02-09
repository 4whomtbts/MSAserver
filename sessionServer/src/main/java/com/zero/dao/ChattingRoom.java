package com.zero.dao;

import com.zero.dto.ChattingMessage;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class ChattingRoom {
    private String roomId;
    private String name;
    private Set<WebSocketSession> sessions = new HashSet<>();

    public static ChattingRoom create(String name) {
        ChattingRoom chattingRoom = new ChattingRoom();
        chattingRoom.roomId = UUID.randomUUID().toString();
        chattingRoom.name = name;
        return chattingRoom;
    }

    public void handleActions(WebSocketSession session, ChattingMessage message) {
    if(message.getType().equals(ChattingMessage.MessageType.JOIN)) {
        sessions.add(session);
        message.setMessage(message.getSender() + "님이 입장했습니다.");
    }
     //sendMessage(message, service);
    }

}
