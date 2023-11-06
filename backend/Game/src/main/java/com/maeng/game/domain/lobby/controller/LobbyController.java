package com.maeng.game.domain.lobby.controller;

import com.maeng.game.domain.lobby.dto.RoomDTO;
import com.maeng.game.domain.lobby.enums.ChannelTire;
import com.maeng.game.domain.lobby.service.LobbyService;
import com.maeng.game.domain.room.entity.Game;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/lobby")
@RequiredArgsConstructor
@RestController
public class LobbyController {

    private final LobbyService lobbyService;
    // 방 리스트 조회
    @GetMapping("/{game}/{channelTire}")
    public ResponseEntity<List<RoomDTO>> findRoomList(@PathVariable Game game, @PathVariable ChannelTire channelTire){
        List<RoomDTO> result = lobbyService.findRoomList(game, channelTire);
        return ResponseEntity.ok(result);
    }
}
