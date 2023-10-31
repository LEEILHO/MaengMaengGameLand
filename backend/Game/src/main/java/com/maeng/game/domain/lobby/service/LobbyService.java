package com.maeng.game.domain.lobby.service;

import com.maeng.game.domain.lobby.enums.ChannelTire;
import com.maeng.game.domain.room.dto.MessageDTO;
import com.maeng.game.domain.room.entity.Game;
import com.maeng.game.domain.room.entity.Room;
import com.maeng.game.domain.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class LobbyService {

    private final RoomRepository roomRepository;
    private final RabbitTemplate template;
    private final static String LOBBY_EXCHANGE_NAME = "room.exchange";

    public void findAllRoom(Game game, ChannelTire channelTire){
        Room room = roomRepository.findById("989322").orElse(null);

        List<Room> roomList = roomRepository.findAllByGameCategory(game);
        log.info(game+" "+channelTire);

        MessageDTO messageDTO = MessageDTO.builder()
                .type("ROOM_LIST")
                .data(roomList)
                .build();

        log.info(messageDTO.toString());

        template.convertAndSend(LOBBY_EXCHANGE_NAME, "lobby"+game+"."+channelTire, messageDTO);
    }

}
