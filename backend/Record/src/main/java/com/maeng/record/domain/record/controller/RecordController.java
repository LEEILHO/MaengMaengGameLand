package com.maeng.record.domain.record.controller;

import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maeng.record.domain.record.dto.GameParticipantDTO;
import com.maeng.record.domain.record.dto.UserGameInfoDTO;
import com.maeng.record.domain.record.dto.WatchGameScoreDTO;
import com.maeng.record.domain.record.service.MmjService;
import com.maeng.record.domain.record.service.RecordService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/record")
public class RecordController {
	private final RecordService recordService;
	private final MmjService mmjService;
	@GetMapping("/history")
	public ResponseEntity<List<UserGameInfoDTO>> gameHistory(@RequestHeader("userEmail") String email) {
		return ResponseEntity.status(HttpStatus.SC_OK).body(recordService.userGameHistory(email));
	}

	@PostMapping("/history/detail/{gameCode}")
	public ResponseEntity<List<GameParticipantDTO>> gameHistoryDetail(@PathVariable("gameCode") String gameCode) {
		return ResponseEntity.status(HttpStatus.SC_OK).body(recordService.gameDetail(gameCode));
	}

	@PostMapping("/watch")
	public ResponseEntity<Void> updateWatchGameRecord(@RequestHeader("userEmail") String email, @RequestBody WatchGameScoreDTO watchGameScoreDTO) {
		mmjService.saveMmjRecord(email, watchGameScoreDTO);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/watch")
	public ResponseEntity<WatchGameScoreDTO> getWatchGameRecord(@RequestHeader("userEmail") String email) {
		WatchGameScoreDTO score = mmjService.getMmjRecord(email);
		return ResponseEntity.ok(score);
	}
}
