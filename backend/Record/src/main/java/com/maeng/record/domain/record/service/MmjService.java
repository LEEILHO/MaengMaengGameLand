package com.maeng.record.domain.record.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maeng.record.domain.record.dto.WatchGameScoreDTO;
import com.maeng.record.domain.record.entity.Mmj;
import com.maeng.record.domain.record.exception.GameUserNotFoundException;
import com.maeng.record.domain.record.repository.GameUserRepository;
import com.maeng.record.domain.record.repository.MmjRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MmjService {
	private final MmjRepository mmjRepository;
	private final GameUserRepository gameUserRepository;

	@Transactional
	public void saveMmjRecord(String email, WatchGameScoreDTO watchGameScoreDTO) {
		Mmj mmj = mmjRepository.findByGameUserEmail(email)
			.orElse(Mmj.builder()
				.gameUser(gameUserRepository.findByEmail(email)
					.orElseThrow(() -> new GameUserNotFoundException("게임 유저를 찾을 수 없습니다.")))
				.build());

		if(mmj.getScore() < watchGameScoreDTO.getScore()) {
			mmj.updateScore(watchGameScoreDTO.getScore());
			mmjRepository.save(mmj);
		}
	}

	@Transactional(readOnly = true)
	public WatchGameScoreDTO getMmjRecord(String email) {
		return WatchGameScoreDTO.builder()
			.score(mmjRepository.findByGameUserEmail(email)
				.orElse(Mmj.builder()
					.score(0)
					.updatedAt(null)
					.build())
				.getScore())
			.build();
	}
}
