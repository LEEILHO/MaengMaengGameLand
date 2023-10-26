package com.maeng.jwac.domain.timer.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.maeng.jwac.domain.timer.service.TimerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/timer")
public class TimerController {
	private final TimerService timerService;

	@PostMapping("/end")
	public void end(@RequestParam String gameCode, String nickname, int round) {
		timerService.timerEnd(gameCode, nickname, round);
	}
}
