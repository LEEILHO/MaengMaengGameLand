package com.maeng.jwac.domain.game.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.maeng.jwac.domain.game.dto.PlayerInfo;
import com.maeng.jwac.domain.game.emums.Jwerly;
import com.maeng.jwac.domain.game.entity.Jwac;
import com.maeng.jwac.domain.game.entity.Player;
import com.maeng.jwac.domain.game.repository.JwacRedisRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GameService {
	private final int MIN_ROUND = 15;
	private final int MAX_ROUND = 25;

	private final JwacRedisRepository jwacRedisRepository;

	public void generateGame(String roomCode, String gameCode, List<PlayerInfo> playerInfo) {
		int maxRound = setRound();

		Jwac jwac = Jwac.builder()
			.gameCode(gameCode)
			.roomCode(roomCode)
			.createAt(LocalDateTime.now())
			.headCount(playerInfo.size())
			.currentRound(0)
			.maxRound(maxRound)
			.bidAmounts(new ArrayList<>())
			.jwerly(setRandomJwerly(maxRound))
			.players(setPlayer(playerInfo))
			.build();

		jwacRedisRepository.save(jwac);
	}

	public void nextRound(String gameCode) {
		// TODO : 현재 라운드 결과

		// TODO : 다음 라운드 시작
	}

	public int getHeadCount(String gameCode) {
		Jwac jwac =  jwacRedisRepository.findById(gameCode).orElse(null);

		if(jwac == null) {
			return 0;
		} else {
			return jwac.getHeadCount();
		}
	}

	public int setRound() {
		return (int)(Math.random() * (MAX_ROUND - MIN_ROUND)) + MIN_ROUND;
	}

	public List<Jwerly> setRandomJwerly(int maxRound) {
		List<Jwerly> jwerly = new ArrayList<>();
		for(int i = 0; i < maxRound; i++) {
			if(i == 10) {
				jwerly.add(Jwerly.SPECIAL);
				continue;
			}
			jwerly.add(Jwerly.values()[(int)(Math.random() * 3)]);
		}
		return jwerly;
	}

	public List<Player> setPlayer(List<PlayerInfo> playerInfo) {
		List<Player> players = new ArrayList<>();
		for(PlayerInfo info : playerInfo) {
			players.add(Player.builder()
					.nickname(info.getNickname())
					.profileUrl(info.getProfileUrl())
					.tier(info.getTier())
					.score(0)
					.totalBidAmount(0)
					.specialItem(false)
					.history(new HashMap<>())
					.build());
		}
		return players;
	}
}
