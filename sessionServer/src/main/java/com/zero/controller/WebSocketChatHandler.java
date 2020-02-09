/*
package com.zero.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zero.dao.ChattingRoom;
import com.zero.dto.ChattingMessage;
import com.zero.service.ChattingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketChatHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final ChattingService service;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("payload {}", payload);
        ChattingMessage chattingMessage = objectMapper.readValue(payload, ChattingMessage.class);
        ChattingRoom room = service.findRoomById(chattingMessage.getRoomId());
        room.handleActions(session, chattingMessage, service);
    }
}
*/