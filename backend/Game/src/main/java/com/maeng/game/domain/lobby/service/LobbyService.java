package com.maeng.game.domain.lobby.service;

import com.maeng.game.domain.lobby.dto.RoomDTO;
import com.maeng.game.domain.lobby.enums.ChannelTire;
import com.maeng.game.domain.room.dto.MessageDTO;
import com.maeng.game.domain.room.entity.Game;
import com.maeng.game.domain.room.entity.Room;
import com.maeng.game.domain.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class LobbyService {

    private final RoomRepository roomRepository;
    private final RabbitTemplate template;
    private final static String LOBBY_EXCHANGE_NAME = "room";

    public void findAllRoom(Game game, ChannelTire channelTire){
        List<Room> roomList = roomRepository.findAllByGameCategoryAndChannelTireAndPublicRoomIsTrue(game, channelTire);
        List<RoomDTO> list = new ArrayList<>();

        for(Room room : roomList){
            list.add(RoomDTO.builder()
                            .roomCode(room.getId())
                            .title(room.getTitle())
                            .headCount(room.getHeadCount())
                            .maxHeadCount(room.getMaxHeadCount())
                            .build());
        }

        MessageDTO messageDTO = MessageDTO.builder()
                .type("ROOM_LIST")
                .data(list)
                .build();

        log.info(messageDTO.toString());

        template.convertAndSend(LOBBY_EXCHANGE_NAME, "lobby."+game+"."+channelTire, messageDTO);
    }
}
