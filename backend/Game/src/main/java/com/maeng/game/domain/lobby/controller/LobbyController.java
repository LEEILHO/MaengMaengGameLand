package com.maeng.game.domain.lobby.controller;

import com.maeng.game.domain.lobby.enums.ChannelTire;
import com.maeng.game.domain.lobby.service.LobbyService;
import com.maeng.game.domain.room.entity.Game;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class LobbyController {

    private final LobbyService lobbyService;
    // 방 리스트 조회
    @MessageMapping("lobby.{game}.{channelTire}")
    public void findAllRoom(@DestinationVariable Game game, @DestinationVariable ChannelTire channelTire){
        lobbyService.findAllRoom(game, channelTire);
    }
}
