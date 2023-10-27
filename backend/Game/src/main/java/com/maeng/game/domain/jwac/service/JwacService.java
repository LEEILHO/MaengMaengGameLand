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
		Jwac jwac = jwacRedisRepository.findById(jwacBidInfoDto.getGameCode())
			.orElseThrow(() -> new GameNotFoundException(jwacBidInfoDto.getGameCode()));

		History history = History.builder()
			.bidAmount(jwacBidInfoDto.getBidAmount())
			.bidAt(LocalDateTime.now())
			.build();
		if(jwac.getPlayers().get(jwacBidInfoDto.getNickname()).getHistory() == null) {
			jwac.getPlayers().get(jwacBidInfoDto.getNickname()).setHistory(new HashMap<>());
		}
		jwac.getPlayers().get(jwacBidInfoDto.getNickname()).getHistory().put(jwacBidInfoDto.getRound(), history);

		jwacRedisRepository.save(jwac);
	}

	@Transactional
	public JwacRoundResultDto endRound(String gameCode) {
		Jwac jwac = jwacRedisRepository.findById(gameCode).orElseThrow(() -> new GameNotFoundException(gameCode));

		return roundResult(jwac);
	}

	@Transactional
	public boolean nextRound(String gameCode) {
		Jwac jwac = jwacRedisRepository.findById(gameCode).orElseThrow(() -> new GameNotFoundException(gameCode));

		if(hasNextRound(jwac)) {
			jwacRedisRepository.save(jwac);
			return true;
		}

		return false;
	}

	@Transactional
	public void endGame(Jwac jwac) {
		// TODO : 게임 종료 로직
	}

	@Transactional(readOnly = true)
	public int getHeadCount(String gameCode) {
		Jwac jwac =  jwacRedisRepository.findById(gameCode).orElseThrow(() -> new GameNotFoundException(gameCode));

		return jwac.getHeadCount();
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

	@Transactional
	public JwacRoundResultDto roundResult(Jwac jwac) {
		JwacRoundResultDto jwacRoundResultDto = new JwacRoundResultDto();
		jwacRoundResultDto.setGameCode(jwac.getGameCode());
		jwacRoundResultDto.setRoundBidSum(-1L);

		int currentRound = jwac.getCurrentRound();

		// Step 1: 관련 데이터 추출
		Map<String, History> result = new HashMap<>();
		for (String nickname : jwac.getPlayers().keySet()) {
			Player player = jwac.getPlayers().get(nickname);
			History playerHistory = player.getHistory().get(currentRound);
			if (playerHistory != null) {
				result.put(nickname, playerHistory);
			}
		}

		// Step 2: 가장 많이 입찰한 사람과 가장 적게 입찰한 사람 찾기
		String mostBidder = findMostBidder(result);
		String leastBidder = findLeastBidder(result);

		// Step 3: jwacRoundResultDto 업데이트
		jwacRoundResultDto.setMostBidder(mostBidder);
		jwacRoundResultDto.setLeastBidder(leastBidder);

		// Step 4: 15 라운드 특수 처리
		long mostBidAmount = result.get(mostBidder).getBidAmount();
		if (currentRound == 15) {
			handleRound15(jwac, mostBidder, mostBidAmount);
		} else {
			handleNonRound15(jwac, mostBidder, leastBidder, currentRound, mostBidAmount);
		}

		// Step 5: 플레이어 점수를 jwacRoundResult에 저장
		// TODO : 플레이어 점수 jwacRoundResult 에 저장

		return jwacRoundResultDto;
	}

	private String findMostBidder(Map<String, History> result) {
		String mostBidder = "";
		long mostBidAmount = -1;
		LocalDateTime mostBidderBidAt = LocalDateTime.now();

		for (String userEmail : result.keySet()) {
			History currentItem = result.get(userEmail);
			long bidAmount = currentItem.getBidAmount();
			LocalDateTime bidAt = currentItem.getBidAt();

			if (bidAmount > mostBidAmount || (bidAmount == mostBidAmount && bidAt.isAfter(mostBidderBidAt))) {
				mostBidder = userEmail;
				mostBidAmount = bidAmount;
				mostBidderBidAt = bidAt;
			}
		}

		return mostBidder;
	}

	private String findLeastBidder(Map<String, History> result) {
		String leastBidder = "";
		long leastBidAmount = Long.MAX_VALUE;
		LocalDateTime leastBidderBidAt = LocalDateTime.of(1970, 1, 1, 0, 0, 0);

		for (String userEmail : result.keySet()) {
			History currentItem = result.get(userEmail);
			long bidAmount = currentItem.getBidAmount();
			LocalDateTime bidAt = currentItem.getBidAt();

			if (bidAmount < leastBidAmount || (bidAmount == leastBidAmount && bidAt.isBefore(leastBidderBidAt))) {
				leastBidder = userEmail;
				leastBidAmount = bidAmount;
				leastBidderBidAt = bidAt;
			}
		}

		return leastBidder;
	}

	private void handleRound15(Jwac jwac, String mostBidder, Long mostBidAmount) {
		if (!mostBidder.isEmpty()) {
			Player mostBidderPlayer = jwac.getPlayers().get(mostBidder);
			mostBidderPlayer.getHistory().get(jwac.getCurrentRound()).roundWin();
			mostBidderPlayer.addTotalBidAmount(mostBidAmount);
			mostBidderPlayer.addSpecialItem();

			// TODO : 아이템 결과 처리 (미구현)
			Map<Jwerly, Integer> itemResult = useSpecialItem(jwac);
		}
	}

	private void handleNonRound15(Jwac jwac, String mostBidder, String leastBidder, int currentRound, Long mostBidAmount) {
		if (!mostBidder.isEmpty()) {
			Player mostBidderPlayer = jwac.getPlayers().get(mostBidder);
			mostBidderPlayer.getHistory().get(currentRound).roundWin();
			mostBidderPlayer.addTotalBidAmount(mostBidAmount);
			mostBidderPlayer.addScore(getScore(jwac.getJwerly().get(currentRound - 1)));
		}

		if (!leastBidder.isEmpty()) {
			Player leastBidderPlayer = jwac.getPlayers().get(leastBidder);
			leastBidderPlayer.getHistory().get(currentRound).roundLose();
			leastBidderPlayer.addScore(-1);
		}
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

	public boolean hasNextRound(Jwac jwac) {
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
		return jwerly.getIndex();
	}

	public String generateGameCode() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replaceAll("-", "").substring(0, 16);
	}
}
