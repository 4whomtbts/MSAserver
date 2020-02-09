/*
package com.zero.service;

import com.zero.dao.ChattingRoom;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.*;

@Repository
public class ChattingRoomRepository {

    private Map<String, ChattingRoom> chattingRoomMap;

    @PostConstruct
    private void init() {
        chattingRoomMap = new LinkedHashMap<>();
    }

    public List<ChattingRoom> findAllRoom() {
        List chattingRooms = new ArrayList(chattingRoomMap.values());
        Collections.reverse(chattingRooms);
        return chattingRooms;
    }

    public ChattingRoom findRoomById(String id) {
        return chattingRoomMap.get(id);
    }

    public ChattingRoom createChattingRoom(String name) {
        ChattingRoom chattingRoom = ChattingRoom.create(name);
        chattingRoomMap.put(chattingRoom.getRoomId(), chattingRoom);
        return chattingRoom;
    }
}
*/