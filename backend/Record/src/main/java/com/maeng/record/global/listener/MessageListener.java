package com.maeng.record.global.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maeng.record.domain.record.data.Awrsp;
import com.maeng.record.domain.record.data.Jwac;
import com.maeng.record.domain.record.dto.NicknameEditDTO;
import com.maeng.record.domain.record.dto.RankDTO;
import com.maeng.record.domain.record.dto.UserInfoDTO;
import com.maeng.record.domain.record.exception.GameAlreadyExistException;
import com.maeng.record.domain.record.service.AwrspRecordService;
import com.maeng.record.domain.record.service.GameUserService;
import com.maeng.record.domain.record.service.JwacRecordService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageListener {
	private final RabbitTemplate template;
	private final String SCORE_EXCHANGE = "score";

	private final ObjectMapper objectMapper;

	private final GameUserService gameUserService;
	private final JwacRecordService jwacRecordService;
	private final AwrspRecordService awrspRecordService;

	@RabbitListener(queues = "register.queue")
	public void receiveMessage1(UserInfoDTO userInfo){
		log.info("register = " + userInfo);
		gameUserService.registerGameUser(userInfo);
	}

	@RabbitListener(queues = "edit.queue")
	public void receiveMessage2(NicknameEditDTO nicknameEditDTO){
		try {
			log.info("edit = " + nicknameEditDTO);
			gameUserService.editGameUser(nicknameEditDTO);
		} catch (Exception e) {
			log.info(e.getMessage());
		}
	}

	@RabbitListener(queues = "jwac.queue")
	public void receiveMessage2(String message) {
		try {
			log.info("jwac = " + message);
			Jwac jwac = objectMapper.readValue(message, Jwac.class);
			jwacRecordService.saveJwacRecord(jwac);

			RankDTO rankDTO = jwacRecordService.generateRankDTO(jwac);
			template.convertAndSend(SCORE_EXCHANGE, "score.jwac", rankDTO);
			log.info("jwac save success");
		} catch (JsonProcessingException e) {
			log.info("jwac json parsing error");
			log.info(e.getMessage());
			log.info(message);
		} catch (GameAlreadyExistException e) {
			log.info(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@RabbitListener(queues = "gsb.queue")
	public void receiveMessage3(String message){
		log.info("gsb = " + message);
	}

	@RabbitListener(queues = "awrsp.queue")
	public void receiveMessage4(String message) {
		try {
			log.info("awrsp = " + message);
			Awrsp awrap = objectMapper.readValue(message, Awrsp.class);
			awrspRecordService.saveAwrspRecord(awrap);

			RankDTO rankDTO = awrspRecordService.generateRankDTO(awrap);
			template.convertAndSend(SCORE_EXCHANGE, "score.awrsp", rankDTO);
			log.info("awrsp save success");
		} catch (JsonProcessingException e) {
			log.info("awrsp json parsing error");
			log.info(e.getMessage());
			log.info(message);
		} catch (GameAlreadyExistException e) {
			log.info(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
