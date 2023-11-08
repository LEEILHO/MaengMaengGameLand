package com.maeng.record.global.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maeng.record.domain.record.data.Awrsp;
import com.maeng.record.domain.record.data.Jwac;
import com.maeng.record.domain.record.dto.UserInfoDTO;
import com.maeng.record.domain.record.service.AwrspRecordService;
import com.maeng.record.domain.record.service.GameUserService;
import com.maeng.record.domain.record.service.JwacRecordService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageListener {
	private final ObjectMapper objectMapper;

	private final GameUserService gameUserService;
	private final JwacRecordService jwacRecordService;
	private final AwrspRecordService awrspRecordService;

	@RabbitListener(queues = "register.queue")
	public void receiveMessage1(UserInfoDTO userInfo){
		log.info("register = " + userInfo);
		gameUserService.registerGameUser(userInfo);
	}

	@RabbitListener(queues = "record.queue")
	public void receiveMessage(String message){
		// System.out.println("record = " + message);
	}

	@RabbitListener(queues = "jwac.queue")
	public void receiveMessage2(String message){
		Jwac jwac = null;
		try {
			jwac = objectMapper.readValue(message, Jwac.class);
			jwacRecordService.saveJwacRecord(jwac);
		} catch (Exception e) {
			System.out.println("jwac json parsing error");
		}

	}

	@RabbitListener(queues = "gsb.queue")
	public void receiveMessage3(String message){
		System.out.println("gsb = " + message);
	}

	@RabbitListener(queues = "awrsp.queue")
	public void receiveMessage4(String message){
		Awrsp awrap = null;
		try {
			awrap = objectMapper.readValue(message, Awrsp.class);
			awrspRecordService.saveAwrspRecord(awrap);
		} catch (Exception e) {
			System.out.println("jwac json parsing error");
		}
	}
}
