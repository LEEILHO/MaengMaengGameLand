package com.maeng.game.domain.jwac.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maeng.game.domain.jwac.dto.JwacBidInfoDto;
import com.maeng.game.domain.jwac.dto.JwacRoundResultDto;
import com.maeng.game.domain.jwac.dto.PlayerInfo;
import com.maeng.game.domain.jwac.emums.Jwerly;
import com.maeng.game.domain.jwac.entity.History;
import com.maeng.game.domain.jwac.entity.Jwac;
import com.maeng.game.domain.jwac.entity.Player;
import com.maeng.game.domain.jwac.exception.GameNotFoundException;
import com.maeng.game.domain.jwac.repository.JwacRedisRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwacService {
	private final int MIN_ROUND = 15;
	private final int MAX_ROUND = 25;

	private final JwacRedisRepository jwacRedisRepository;

	public void generateGame(String roomCode, List<PlayerInfo> playerInfo) {
		String gameCode = generateGameCode();

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

		// TODO : 게임 생성 알림
	}

	@Transactional
	public void bidJwerly(JwacBidInfoDto jwacBidInfoDto) {
		Jwac jwac = jwacRedisRepository.findById(jwacBidInfoDto.getGameCode()).orElseThrow(() -> new GameNotFoundException(jwacBidInfoDto.getGameCode()));

		History history = History.builder()
			.bidAmount(jwacBidInfoDto.getBidAmount())
			.bidAt(LocalDateTime.now())
			.build();
		if(jwac.getPlayers().get(jwacBidInfoDto.getUerEmail()).getHistory() == null) {
			jwac.getPlayers().get(jwacBidInfoDto.getUerEmail()).setHistory(new HashMap<>());
		}
		jwac.getPlayers().get(jwacBidInfoDto.getUerEmail()).getHistory().put(jwacBidInfoDto.getRound(), history);

		jwacRedisRepository.save(jwac);
	}

	@Transactional
	public JwacRoundResultDto nextRound(String gameCode) {
		boolean hasNextRound = false;

		Jwac jwac = jwacRedisRepository.findById(gameCode).orElseThrow(() -> new GameNotFoundException(gameCode));

		JwacRoundResultDto jwacRoundResult = null;
		if(jwac.getCurrentRound() != 0) {
			jwacRoundResult = roundResult(jwac);
			// TODO : 라운드 결과 알림

			hasNextRound = nextRound(jwac);
		}

		jwacRedisRepository.save(jwac);

		return jwacRoundResult;
	}

	@Transactional
	public void endGame(Jwac jwac) {
		// TODO : 게임 종료 로직
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
		jwerly.add(null);
		for(int i = 1; i <= maxRound; i++) {
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
			players.put(info.getUserEmail(), Player.builder()
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

	@Transactional
	public JwacRoundResultDto roundResult(Jwac jwac) {
		JwacRoundResultDto jwacRoundResultDto = new JwacRoundResultDto();
		jwacRoundResultDto.setGameCode(jwac.getGameCode());
		jwacRoundResultDto.setRoundBidSum(-1L);

		int currentRound = jwac.getCurrentRound();

		Map<String, History> result = new HashMap<>();
		for(String userEmail : jwac.getPlayers().keySet()) {
			if(jwac.getPlayers().get(userEmail).getHistory() == null) {
				continue;
			}
			if(!jwac.getPlayers().get(userEmail).getHistory().containsKey(currentRound)) {
				continue;
			}

			result.put(userEmail, jwac.getPlayers().get(userEmail).getHistory().get(currentRound));
		}

		String mostBidder = "";
		String leastBidder = "";
		long mostBidAmount = -1;
		long leastBidAmount = Long.MAX_VALUE;
		LocalDateTime mostBidderBidAt = LocalDateTime.now();
		LocalDateTime leastBidderBidAt = LocalDateTime.of(1970, 1, 1, 0, 0, 0);
		for (String userEmail : result.keySet()) {
			History currentItem = result.get(userEmail);
			long bidAmount = currentItem.getBidAmount();
			LocalDateTime bidAt = currentItem.getBidAt();

			if (bidAmount > mostBidAmount || (bidAmount == mostBidAmount && bidAt.isAfter(mostBidderBidAt))) {
				mostBidder = userEmail;
				mostBidAmount = bidAmount;
				mostBidderBidAt = bidAt;
			}

			if (bidAmount < leastBidAmount || (bidAmount == leastBidAmount && bidAt.isBefore(leastBidderBidAt))) {
				leastBidder = userEmail;
				leastBidAmount = bidAmount;
				leastBidderBidAt = bidAt;
			}
		}

		jwacRoundResultDto.setMostBidder(mostBidder);
		jwacRoundResultDto.setLeastBidder(leastBidder);

		if (!mostBidder.isEmpty()) {
			// 최고 금액 입찰자의 Player 객체 가져오기
			Player mostBidderPlayer = jwac.getPlayers().get(mostBidder);
			mostBidderPlayer.getHistory().get(currentRound).roundWin();
			mostBidderPlayer.addTotalBidAmount(mostBidAmount);

			if (currentRound == 15) {
				// 15 라운드에서는 최고 금액 입찰자에게 특별 아이템 부여
				mostBidderPlayer.addSpecialItem();

				// TODO : 아이템 결과 알림 처리 (미구현)
				Map<Jwerly, Integer> itemResult = useSpecialItem(jwac);
			} else {
				// 15 라운드 이외에서는 점수를 계산하여 추가
				mostBidderPlayer.addScore(getScore(jwac.getJwerly().get(currentRound - 1)));
			}
		}

		if (!leastBidder.isEmpty() && currentRound != 15) {
			// 최저 금액 입찰자의 Player 객체 가져오기
			Player leastBidderPlayer = jwac.getPlayers().get(leastBidder);
			leastBidderPlayer.getHistory().get(currentRound).roundLose();

			// 15 라운드 이외에서는 최저 금액 입찰자에게 -1 점 부여
			leastBidderPlayer.addScore(-1);
		}

		// TODO : 플레이어 점수 jwacRoundResult 에 저장



		return jwacRoundResultDto;
	}

	public Map<Jwerly, Integer> useSpecialItem(Jwac jwac) {
		List<Jwerly> jwerly = jwac.getJwerly();
		int currentRound = jwac.getCurrentRound();
		int maxRound = jwac.getMaxRound();

		Map<Jwerly, Integer> itemResult = new HashMap<>();
		itemResult.put(Jwerly.SAPPHIRE, 0);
		itemResult.put(Jwerly.RUBY, 0);
		itemResult.put(Jwerly.EMERALD, 0);
		itemResult.put(Jwerly.DIAMOND, 0);

		for(int i = currentRound + 1; i <= maxRound; i++) {
			itemResult.put(jwerly.get(i - 1), itemResult.get(jwerly.get(i - 1)) + 1);
		}

		return itemResult;
	}

	public boolean nextRound(Jwac jwac) {
		int currentRound = jwac.getCurrentRound();
		int maxRound = jwac.getMaxRound();

		if(currentRound >= maxRound) {
			// TODO : 게임 종료 알림
			return false;
		}

		jwac.nextRound();

		return true;
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

	public String generateGameCode() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replaceAll("-", "").substring(0, 16);
	}
}
