package com.maeng.jwac.domain.game.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maeng.jwac.domain.game.dto.PlayerInfo;
import com.maeng.jwac.domain.game.emums.Jwerly;
import com.maeng.jwac.domain.game.entity.History;
import com.maeng.jwac.domain.game.entity.Jwac;
import com.maeng.jwac.domain.game.entity.Player;
import com.maeng.jwac.domain.game.exception.GameNotFoundException;
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

	@Transactional
	public void bidJwerly(String gameCode, String nickname, int round, int bidAmount) {
		Jwac jwac = jwacRedisRepository.findById(gameCode).orElseThrow(() -> new GameNotFoundException(gameCode));

		History history = History.builder()
			.bidAmount(bidAmount)
			.bidAt(LocalDateTime.now())
			.build();

		jwac.getPlayers().get(nickname).getHistory().put(round, history);

		jwacRedisRepository.save(jwac);
	}

	@Transactional
	public void nextRound(String gameCode) {
		Jwac jwac = jwacRedisRepository.findById(gameCode).orElseThrow(() -> new GameNotFoundException(gameCode));

		// TODO : 현재 라운드 결과
		getRoundResult(jwac);

		// TODO : 다음 라운드 시작
		jwac.nextRound();
	}

	@Transactional(readOnly = true)
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

	public Map<String, Player> setPlayer(List<PlayerInfo> playerInfo) {
		Map<String, Player> players = new LinkedHashMap<>();
		for(PlayerInfo info : playerInfo) {
			players.put(info.getNickname(), Player.builder()
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

	public void getRoundResult(Jwac jwac) {
		int currentRound = jwac.getCurrentRound();

		Map<String, History> result = new HashMap<>();
		for(String nickname : jwac.getPlayers().keySet()) {
			result.put(nickname, jwac.getPlayers().get(nickname).getHistory().get(currentRound));
		}

		String mostBidder = "";
		String leastBidder = "";
		long mostBidAmount = -1;
		long leastBidAmount = Long.MAX_VALUE;
		LocalDateTime mostBidderBidAt = LocalDateTime.now();
		LocalDateTime leastBidderBidAt = LocalDateTime.of(1970, 1, 1, 0, 0, 0);
		for (String nickname : result.keySet()) {
			long bidAmount = result.get(nickname).getBidAmount();
			if (mostBidAmount < bidAmount) {
				mostBidder = nickname;
				mostBidAmount = bidAmount;
				mostBidderBidAt = result.get(nickname).getBidAt();
			} else if(mostBidAmount == bidAmount) {
				if(mostBidderBidAt.isAfter(result.get(nickname).getBidAt())) {
					mostBidder = nickname;
					mostBidderBidAt = result.get(nickname).getBidAt();
				}
			}
			if (leastBidAmount > bidAmount) {
				leastBidder = nickname;
				leastBidAmount = bidAmount;
				leastBidderBidAt = result.get(nickname).getBidAt();
			} else if(leastBidAmount == bidAmount) {
				if(leastBidderBidAt.isAfter(result.get(nickname).getBidAt())) {
					leastBidder = nickname;
					leastBidderBidAt = result.get(nickname).getBidAt();
				}
			}
		}

		if(currentRound == 15) {
			jwac.getPlayers().get(mostBidder).getHistory().get(currentRound).roundWin();
			jwac.getPlayers().get(mostBidder).addTotalBidAmount(mostBidAmount);
			jwac.getPlayers().get(mostBidder).addSpecialItem();
		} else {
			// 최고 금액 입찰자
			jwac.getPlayers().get(mostBidder).getHistory().get(currentRound).roundWin();
			jwac.getPlayers().get(mostBidder).addTotalBidAmount(mostBidAmount);
			jwac.getPlayers().get(mostBidder).addScore(getScore(jwac.getJwerly().get(currentRound - 1)));

			// 최저 금액 입찰자 -1점
			jwac.getPlayers().get(leastBidder).getHistory().get(currentRound).roundLose();
			jwac.getPlayers().get(leastBidder).addScore(-1);
		}
	}

	public int getScore(Jwerly jwerly) {
		if(jwerly.equals(Jwerly.SAPPHIRE)) {
			return 1;
		} else if(jwerly.equals(Jwerly.RUBY)) {
			return 2;
		} else if(jwerly.equals(Jwerly.EMERALD)) {
			return 3;
		} else if(jwerly.equals(Jwerly.DIAMOND)) {
			return 4;
		} else {
			return 0;
		}
	}
}
