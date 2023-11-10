package com.maeng.record.domain.record.controller;

import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maeng.record.domain.record.dto.GameParticipantDTO;
import com.maeng.record.domain.record.dto.GameCodeDTO;
import com.maeng.record.domain.record.dto.UserGameInfoDTO;
import com.maeng.record.domain.record.service.RecordService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/record")
public class RecordController {
	private final RecordService recordService;
	@PostMapping("/history")
	public ResponseEntity<List<UserGameInfoDTO>> gameHistory(@RequestHeader("userEmail") String nickname) {
		return ResponseEntity.status(HttpStatus.SC_OK).body(recordService.userGameHistory(nickname));
	}

	@PostMapping("/history/detail")
	public ResponseEntity<List<GameParticipantDTO>>  gameHistoryDetail(@RequestBody GameCodeDTO gameCodeDTO) {
		return ResponseEntity.status(HttpStatus.SC_OK).body(recordService.gameDetail(gameCodeDTO.getGameCode()));
	}
}
