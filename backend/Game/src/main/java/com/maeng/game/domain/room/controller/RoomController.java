package com.maeng.game.domain.room.controller;

import com.maeng.game.domain.room.dto.CreateRoomDTO;
import com.maeng.game.domain.room.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/room")
public class RoomController {
    private final RoomService roomService;

    @Operation(summary = "대기실 생성")
    @PostMapping("/create")
    public ResponseEntity<String> createRoom(@RequestBody CreateRoomDTO createRoomDTO){
        String roomCode = roomService.createRoom(createRoomDTO);
        roomService.enterRoom(roomCode, createRoomDTO.getHost());

        return ResponseEntity.ok(roomCode);
    }


}
