package com.maeng.game.domain.jwac.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.maeng.game.domain.jwac.dto.JwacBidInfoDto;
import com.maeng.game.domain.jwac.dto.JwacGameResultDTO;
import com.maeng.game.domain.jwac.dto.JwacItemResultDTO;
import com.maeng.game.domain.jwac.dto.JwacRank;
import com.maeng.game.domain.jwac.dto.JwacRoundPlayerInfoDTO;
import com.maeng.game.domain.jwac.dto.JwacRoundResultDto;
import com.maeng.game.domain.jwac.dto.PlayerInfo;
import com.maeng.game.domain.jwac.emums.Jewelry;
import com.maeng.game.domain.jwac.entity.History;
import com.maeng.game.domain.jwac.entity.Jwac;
import com.maeng.game.domain.jwac.entity.Player;
import com.maeng.game.domain.jwac.exception.GameNotFoundException;
import com.maeng.game.domain.jwac.repository.JwacRedisRepository;
import com.maeng.game.domain.room.dto.GameStartDTO;
import com.maeng.game.domain.room.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwacService {
	@Value("${game.jwac.round.min}")
	private int MIN_ROUND;

	@Value("${game.jwac.round.max}")
	private int MAX_ROUND;

	@Value("${game.jwac.round.special}")
	private int SPECIAL_ROUND;

	private final TimerService timerService;
	private final EnterService enterService;

	private final JwacRedisRepository jwacRedisRepository;

	@Transactional
	public boolean gameSetting(GameStartDTO gameStartDTO) {
		int maxRound = setRound();

		Jwac jwac = Jwac.builder()
			.gameCode(gameStartDTO.getGameCode())
			.roomCode(gameStartDTO.getRoomCode())
			.createAt(LocalDateTime.now())
			.headCount(gameStartDTO.getHeadCount())
			.currentRound(0)
			.maxRound(maxRound)
			.bidAmounts(new HashMap<>())
			.jewelry(setRandomJewelry(maxRound))
			.players(setPlayer(gameStartDTO.getParticipant()))
			.build();

		jwacRedisRepository.save(jwac);

		timerService.timerCreate(gameStartDTO.getGameCode());
		enterService.enterCreate(gameStartDTO.getGameCode());

		return true;
	}

	@Transactional(readOnly = true)
	public List<PlayerInfo> getGamePlayer(String gameCode) {
		Jwac jwac = jwacRedisRepository.findById(gameCode).orElseThrow(() -> new GameNotFoundException(gameCode));
		return getGamePlayerInfo(jwac.getPlayers());
	}

	@Transactional
	public void bidJewelry(String gameCode, JwacBidInfoDto jwacBidInfoDto) {
		Jwac jwac = jwacRedisRepository.findById(gameCode).orElseThrow(() -> new GameNotFoundException(gameCode));

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
	public Jewelry nextRound(String gameCode) {
		Jwac jwac = jwacRedisRepository.findById(gameCode).orElseThrow(() -> new GameNotFoundException(gameCode));

		if(hasNextRound(jwac)) {
			jwacRedisRepository.save(jwac);
			return jwac.getJewelry().get(jwac.getCurrentRound());
		}

		return null;
	}

	@Transactional
	public Jewelry gameStart(String gameCode) {
		Jwac jwac = jwacRedisRepository.findById(gameCode).orElseThrow(() -> new GameNotFoundException(gameCode));
		jwac.setCurrentRound(1);
		jwacRedisRepository.save(jwac);
		return jwac.getJewelry().get(jwac.getCurrentRound());
	}

	@Transactional
	public JwacGameResultDTO endGame(String gameCode) {
		Jwac jwac = jwacRedisRepository.findById(gameCode).orElseThrow(() -> new GameNotFoundException(gameCode));

		Map<String, Player> players = jwac.getPlayers();

		List<String> rank = findGameRank(players);
		System.out.println(rank.toString());

		jwac.setRank(rank);

		return JwacGameResultDTO.builder()
			.roomCode(jwac.getRoomCode())
			.gameCode(jwac.getGameCode())
			.rank(rank)
			.players(getRoundPlayerInfo(players))
			.build();
	}

	@Transactional(readOnly = true)
	public int getHeadCount(String gameCode) {
		Jwac jwac =  jwacRedisRepository.findById(gameCode).orElseThrow(() -> new GameNotFoundException(gameCode));

		return jwac.getHeadCount();
	}

	public int setRound() {
		return (int)(Math.random() * (MAX_ROUND - MIN_ROUND)) + MIN_ROUND;
	}

	public Map<Integer, Jewelry> setRandomJewelry(int maxRound) {
		Map<Integer, Jewelry> jewelry = new HashMap<>();
		for(int i = 1; i <= maxRound; i++) {
			if(i == SPECIAL_ROUND) {
				jewelry.put(i, Jewelry.SPECIAL);
				continue;
			}
			jewelry.put(i, Jewelry.values()[(int)(Math.random() * 4) + 1]);
		}
		return jewelry;
	}

	public Map<String, Player> setPlayer(List<User> playerInfo) {
		return playerInfo.stream()
			.collect(Collectors.toMap(User::getNickname,
				user -> Player.builder()
					.profileUrl(user.getProfileUrl())
					.tier(user.getTier())
					.score(0)
					.totalBidAmount(0)
					.specialItem(false)
					.history(new HashMap<>())
					.build()));
	}

	@Transactional
	public JwacRoundResultDto roundResult(Jwac jwac) {
		JwacRoundResultDto jwacRoundResultDto = JwacRoundResultDto.builder()
			.gameCode(jwac.getGameCode())
			.roundBidSum(-1L)
			.round(jwac.getCurrentRound())
			.build();

		int currentRound = jwac.getCurrentRound();

		// Step 1: 관련 데이터 추출
		Map<String, History> result = new HashMap<>();
		for (String nickname : jwac.getPlayers().keySet()) {
			result.put(nickname, null);
			Player player = jwac.getPlayers().get(nickname);
			if(player.getHistory() == null) {
				continue;
			}
			History playerHistory = player.getHistory().getOrDefault(currentRound, null);
			if (playerHistory != null) {
				result.put(nickname, playerHistory);
			}
		}

		// Step 2: 가장 많이 입찰한 사람과 가장 적게 입찰한 사람 찾기
		String mostBidder = findMostBidder(result);
		String leastBidder = findLeastBidder(result, jwac);

		// Step 3: jwacRoundResultDto 업데이트
		jwacRoundResultDto.setMostBidder(mostBidder);
		jwacRoundResultDto.setLeastBidder(leastBidder);

		// Step 4: 가장 많이 입찰한 사람과 가장 적게 입찰한 사람에게 점수 부여
		if(result.get(mostBidder) != null) {
			long mostBidAmount = result.get(mostBidder).getBidAmount();
			if(jwac.getBidAmounts() == null) {
				jwac.setBidAmounts(new HashMap<>());
			}
			jwac.getBidAmounts().put(currentRound, mostBidAmount);

			giveScore(jwac, mostBidder, leastBidder, currentRound, mostBidAmount);

		}

		// Step 5: 플레이어 점수를 jwacRoundResult에 저장
		jwacRoundResultDto.setPlayers(getRoundPlayerInfo(jwac.getPlayers()));

		// Step 6: 4라운드 마다 4라운드 동안의 입찰금 합계를 jwacRoundResult에 저장
		Long bidSum = 0L;
		if (currentRound >= 4) {
			if(jwac.getBidAmounts() != null) {
				int start = (currentRound / 4 - 1) * 4 + 1;
				for (int i = start; i < start + 4; i++) {
					bidSum += jwac.getBidAmounts().getOrDefault(i, 0L);
				}
				jwacRoundResultDto.setRoundBidSum(bidSum);
			}
		}

		jwacRedisRepository.save(jwac);


		return jwacRoundResultDto;
	}

	private List<PlayerInfo> getGamePlayerInfo(Map<String, Player> players) {
		return players.entrySet().stream()
			.map(entry -> PlayerInfo.builder()
				.nickname(entry.getKey())
				.profileUrl(entry.getValue().getProfileUrl())
				.tier(entry.getValue().getTier())
				.build())
			.collect(Collectors.toList());
	}

	private List<JwacRoundPlayerInfoDTO> getRoundPlayerInfo(Map<String, Player> players) {
		return players.keySet().stream()
			.map(nickname -> {
				Player player = players.get(nickname);
				return JwacRoundPlayerInfoDTO.builder()
					.nickname(nickname)
					.score(player.getScore())
					.bidSum(player.getTotalBidAmount())
					.item(player.isSpecialItem())
					.build();
			})
			.collect(Collectors.toList());
	}

	private String findMostBidder(Map<String, History> result) {
		String mostBidder = "";
		long mostBidAmount = -1;
		LocalDateTime mostBidderBidAt = LocalDateTime.now();

		for (String nickname : result.keySet()) {
			if(result.get(nickname) == null) {
				continue;
			}
			History currentItem = result.get(nickname);
			long bidAmount = currentItem.getBidAmount();
			LocalDateTime bidAt = currentItem.getBidAt();

			if (bidAmount > mostBidAmount || (bidAmount == mostBidAmount && bidAt.isAfter(mostBidderBidAt))) {
				mostBidder = nickname;
				mostBidAmount = bidAmount;
				mostBidderBidAt = bidAt;
			}
		}

		return mostBidder;
	}

	private String findLeastBidder(Map<String, History> result, Jwac jwac) {
		String leastBidder = "";
		long leastBidAmount = Long.MAX_VALUE;
		LocalDateTime leastBidderBidAt = LocalDateTime.of(1970, 1, 1, 0, 0, 0);

		for (String nickname : result.keySet()) {
			History currentItem = result.get(nickname);
			if(currentItem == null) {
				jwac.getPlayers().get(nickname).addScore(-1);
				leastBidder = "";
				leastBidAmount = -1;
				continue;
			}
			long bidAmount = currentItem.getBidAmount();
			LocalDateTime bidAt = currentItem.getBidAt();

			if (bidAmount < leastBidAmount || (bidAmount == leastBidAmount && bidAt.isBefore(leastBidderBidAt))) {
				leastBidder = nickname;
				leastBidAmount = bidAmount;
				leastBidderBidAt = bidAt;
			}
		}

		return leastBidder;
	}


	private void giveScore(Jwac jwac, String mostBidder, String leastBidder, int currentRound, Long mostBidAmount) {
		if (!mostBidder.isEmpty()) {
			Player mostBidderPlayer = jwac.getPlayers().get(mostBidder);
			mostBidderPlayer.getHistory().get(currentRound).roundWin();
			mostBidderPlayer.addTotalBidAmount(mostBidAmount);
			mostBidderPlayer.addScore(getScore(jwac.getJewelry().get(currentRound)));
		}

		if (!leastBidder.isEmpty()) {
			Player leastBidderPlayer = jwac.getPlayers().get(leastBidder);
			leastBidderPlayer.getHistory().get(currentRound).roundLose();
			leastBidderPlayer.addScore(-1);
		}
	}

	public JwacItemResultDTO getSpecialItemResult(String gameCode, String mostBidder) {
		Jwac jwac = jwacRedisRepository.findById(gameCode).orElseThrow(() -> new GameNotFoundException(gameCode));
		Map<Integer, Jewelry> jewelry = jwac.getJewelry();
		int currentRound = jwac.getCurrentRound();
		int maxRound = jwac.getMaxRound();

		Map<Jewelry, Integer> itemResult = new HashMap<>();
		itemResult.put(Jewelry.SAPPHIRE, 0);
		itemResult.put(Jewelry.RUBY, 0);
		itemResult.put(Jewelry.EMERALD, 0);
		itemResult.put(Jewelry.DIAMOND, 0);

		for(int i = currentRound; i <= maxRound; i++) {
			itemResult.put(jewelry.get(i), itemResult.get(jewelry.get(i)) + 1);
		}

		return JwacItemResultDTO.builder()
			.itemResult(itemResult)
			.nickname(mostBidder)
			.build();
	}

	public boolean hasNextRound(Jwac jwac) {
		int currentRound = jwac.getCurrentRound();
		int maxRound = jwac.getMaxRound();

		if(currentRound >= maxRound) {
			return false;
		}

		jwac.nextRound();

		return true;
	}

	public int getScore(Jewelry jewelry) {
		return jewelry.getIndex();
	}

	public List<String> findGameRank(Map<String, Player> players) {
		List<String> rank = new ArrayList<>();
		long maxBidAmount = 0L;
		String maxBidder = "";
		PriorityQueue<JwacRank> pq = new PriorityQueue<>();
		for(String nickname : players.keySet()) {
			Player player = players.get(nickname);
			pq.add(JwacRank.builder()
				.nickname(nickname)
				.score(player.getScore())
				.totalBidAmount(player.getTotalBidAmount())
				.build());

			if(maxBidAmount < player.getTotalBidAmount()) {
				maxBidAmount = player.getTotalBidAmount();
				maxBidder = nickname;
			}
		}

		while(!pq.isEmpty()) {
			JwacRank jwacRank = pq.poll();
			// 배팅금을 가장 많이 사용한 사람
			if(!jwacRank.getNickname().equals(maxBidder)) {
				rank.add(jwacRank.getNickname());
			}
		}
		rank.add(maxBidder);

		return rank;
	}

	public String getAllDataToJson(String gameCode) {
		Jwac jwac = jwacRedisRepository.findById(gameCode).orElseThrow(() -> new GameNotFoundException(gameCode));
		String json = "";
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.registerModule(new JavaTimeModule());
			objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			json = objectMapper.writeValueAsString(jwac);
		} catch (Exception e) {
			log.error("json error : {}", e.getMessage());
		}

		return json;
	}
}
