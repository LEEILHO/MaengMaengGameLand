package com.maeng.game.domain.room.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maeng.game.domain.room.dto.CreateRoomDTO;
import com.maeng.game.domain.room.dto.RoomCodeDTO;
import com.maeng.game.domain.room.service.RoomService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/room")
@RestController
public class RoomController {

    private final RoomService roomService;

    @Operation(summary = "대기방 생성")
    @PostMapping("/create")
    public ResponseEntity<RoomCodeDTO> create(@RequestBody CreateRoomDTO createRoomDTO){

        String roomCode = roomService.createRoom(createRoomDTO);
        log.info(roomCode);

        return ResponseEntity.ok(RoomCodeDTO.builder().roomCode(roomCode).build());
    }

}
