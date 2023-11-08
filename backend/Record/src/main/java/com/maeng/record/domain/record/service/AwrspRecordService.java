package com.maeng.record.domain.record.service;

import org.springframework.stereotype.Service;

import com.maeng.record.domain.record.repository.AwrspGameAnswerRepository;
import com.maeng.record.domain.record.repository.AwrspRoundDataRepository;
import com.maeng.record.domain.record.repository.AwrspRoundRepository;
import com.maeng.record.domain.record.repository.GameRepository;
import com.maeng.record.domain.record.repository.GameUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AwrspRecordService {
	private final GameRepository gameRepository;
	private final GameUserRepository gameUserRepository;
	private final AwrspRoundRepository awrspRoundRepository;
	private final AwrspRoundDataRepository awrspRoundDataRepository;
	private final AwrspGameAnswerRepository awrspGameAnswerRepository;


}
