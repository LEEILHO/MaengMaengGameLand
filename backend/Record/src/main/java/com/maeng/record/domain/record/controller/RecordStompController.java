package com.maeng.record.domain.record.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.maeng.record.domain.record.data.Jwac;

@Controller
public class RecordStompController {

	@MessageMapping("record.jwac.{gameCode}")
	public void record(@DestinationVariable String gameCode, Jwac jwac) {
		System.out.println(gameCode + " = " + jwac);
	}
}
