package com.maeng.record.domain.record.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maeng.record.domain.record.data.Jwac;
import com.maeng.record.domain.record.service.JwacRecordService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/record")
public class RecordController {
	private final JwacRecordService jwacRecordService;
	@PostMapping("/jwac")
	public void recordJwac(@RequestBody Jwac jwac) {
		System.out.println("jwac = " + jwac);
		jwacRecordService.saveJwacRecord(jwac);
	}
}
