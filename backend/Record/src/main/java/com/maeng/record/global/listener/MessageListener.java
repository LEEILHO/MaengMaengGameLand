package com.maeng.record.global.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.maeng.record.domain.record.data.Jwac;
import com.maeng.record.domain.record.service.JwacRecordService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageListener {
	private final JwacRecordService jwacRecordService;

	@RabbitListener(queues = "record.queue")
	public void receiveMessage(String message){
		// System.out.println("record = " + message);
	}

	@RabbitListener(queues = "jwac.queue")
	public void receiveMessage2(String message){
		Jwac jwac = null;
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.registerModule(new JavaTimeModule());
			objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			jwac = objectMapper.readValue(message, Jwac.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		jwacRecordService.saveJwacRecord(jwac);
	}

	@RabbitListener(queues = "gsb.queue")
	public void receiveMessage3(String message){
		System.out.println("gsb = " + message);
	}

	@RabbitListener(queues = "awrsp.queue")
	public void receiveMessage4(String message){
		System.out.println("awrsp = " + message);
	}
}
