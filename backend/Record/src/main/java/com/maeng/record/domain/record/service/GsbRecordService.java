package com.maeng.record.domain.record.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.maeng.record.domain.record.data.Gsb;
import com.maeng.record.domain.record.dto.RankDTO;
import com.maeng.record.domain.record.entity.Game;
import com.maeng.record.domain.record.entity.GameParticipant;
import com.maeng.record.domain.record.entity.GameUser;
import com.maeng.record.domain.record.entity.GsbRound;
import com.maeng.record.domain.record.entity.GsbRoundHistory;
import com.maeng.record.domain.record.enums.GameCategoty;
import com.maeng.record.domain.record.enums.WinDrawLose;
import com.maeng.record.domain.record.exception.GameAlreadyExistException;
import com.maeng.record.domain.record.repository.GameParticipantRepository;
import com.maeng.record.domain.record.repository.GameRepository;
import com.maeng.record.domain.record.repository.GameUserRepository;
import com.maeng.record.domain.record.repository.GsbRoundHistoryRepository;
import com.maeng.record.domain.record.repository.GsbRoundRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class GsbRecordService {
	private final GameRepository gameRepository;
	private final GameUserRepository gameUserRepository;
	private final GsbRoundRepository gsbRoundRepository;
	private final GsbRoundHistoryRepository gsbRoundHistoryRepository;
	private final GameParticipantRepository gameParticipantRepository;

	public void saveGsbRecord(Gsb gsb) {
		Game game = createGame(gsb.getGameCode(), gsb.getCreateAt());

		Map<String, GameParticipant> participants = createGameParticipant(game, new ArrayList<>(gsb.getPlayers().values()), gsb);

		Map<Integer, GsbRound> gsbRounds = createGsbRound(game, participants, gsb);

		List<GsbRoundHistory> gsbRoundHistories = createGsbRoundHistory(gsb.getPlayers(), participants, gsbRounds);

		gameRepository.save(game);
		gameParticipantRepository.saveAll(participants.values());
		gsbRoundRepository.saveAll(gsbRounds.values());
		gsbRoundHistoryRepository.saveAll(gsbRoundHistories);
	}

	private Game createGame(String gameCode, LocalDateTime startAt) {
		if(gameRepository.existsById(gameCode)) {
			throw new GameAlreadyExistException(gameCode);
		}
		return Game.builder()
			.gameCode(gameCode)
			.gameCategory(GameCategoty.GOLD_SILVER_BRONZE)
			.startAt(startAt)
			.endAt(LocalDateTime.now())
			.build();
	}

	private Map<String, GameParticipant> createGameParticipant(Game game, List<Gsb.Player> participants, Gsb gsb) {
		Map<String, WinDrawLose> results = gsb.getResults().stream()
			.collect(Collectors.toMap(
				Gsb.Result::getNickname,
				Gsb.Result::getWinDrawLose
			));

		List<GameParticipant> gameParticipants = new ArrayList<>();
		for(Gsb.Player player : participants) {
			String nickname = player.getNickname();
			GameUser gameUser = getOrCreateUser(nickname);
			int rank = results.get(nickname).equals(WinDrawLose.WIN) ? 1 : 2;
			GameParticipant gameParticipant = GameParticipant.builder()
				.game(game)
				.gameUser(gameUser)
				.userRank(rank)
				.build();

			gameParticipants.add(gameParticipant);
		}

		return gameParticipants.stream()
			.collect(Collectors.toMap(
				gameParticipant -> gameParticipant.getGameUser().getNickname(),
				Function.identity()
			));

	}

	private GameUser getOrCreateUser(String participant) {
		GameUser gameUser = gameUserRepository.findByNickname(participant)
			.orElse(null);

		if(gameUser == null) {
			gameUser = GameUser.builder()
				.nickname(participant)
				.build();
			gameUserRepository.save(gameUser);
		}

		return gameUser;
	}

	private Map<Integer, GsbRound> createGsbRound(Game game, Map<String, GameParticipant> participants, Gsb gsb) {
		int maxRound = gsb.getCurrentRound();
		String firstPlayer = gsb.getStartCards()[0].getSeq() == 0 ? gsb.getStartCards()[0].getNickname() : gsb.getStartCards()[1].getNickname();
		String secondPlayer = gsb.getStartCards()[0].getSeq() == 0 ? gsb.getStartCards()[1].getNickname() : gsb.getStartCards()[0].getNickname();

		Map<Integer, Gsb.History> firstPlayerHistory = gsb.getPlayers().get(0).getNickname().equals(firstPlayer) ? gsb.getPlayers().get(0).getHistories() : gsb.getPlayers().get(1).getHistories();
		Map<Integer, Gsb.History> secondPlayerHistory = gsb.getPlayers().get(0).getNickname().equals(firstPlayer) ? gsb.getPlayers().get(1).getHistories() : gsb.getPlayers().get(0).getHistories();

		Map<Integer, GsbRound> gsbRounds = new HashMap<>();
		for(int round = 1; round <= maxRound; round++) {
			if(firstPlayerHistory.get(round) == null || secondPlayerHistory.get(round) == null) {
				continue;
			}
			GameParticipant roundWinner = firstPlayerHistory.get(round).getWinDrawLose().equals(WinDrawLose.WIN) ?
				participants.get(firstPlayer) :
				(secondPlayerHistory.get(round).getWinDrawLose().equals(WinDrawLose.WIN) ?
					participants.get(secondPlayer) : null);
			int chip = firstPlayerHistory.get(round).getBettingChips() + secondPlayerHistory.get(round).getBettingChips();

			gsbRounds.put(round, GsbRound.builder()
				.game(game)
				.round(round)
				.roundWinner(roundWinner)
				.chip(chip)
				.build());
		}

		return gsbRounds;
	}

	private List<GsbRoundHistory> createGsbRoundHistory(Map<Integer, Gsb.Player> players, Map<String, GameParticipant> participants, Map<Integer, GsbRound> gsbRounds) {
		List<GsbRoundHistory> gsbRoundHistories = new ArrayList<>();
		for(int round = 1; round <= gsbRounds.size(); round++) {
			Gsb.Player firstPlayer = players.get(0);
			Gsb.Player secondPlayer = players.get(1);
			GsbRound gsbRound = gsbRounds.get(round);

			gsbRoundHistories.add(GsbRoundHistory.builder()
				.gameParticipant(participants.get(firstPlayer.getNickname()))
				.gsbRound(gsbRound)
				.gold(firstPlayer.getHistories().get(round).getGold())
				.silver(firstPlayer.getHistories().get(round).getSilver())
				.bronze(firstPlayer.getHistories().get(round).getBronze())
				.winDrawLose(firstPlayer.getHistories().get(round).getWinDrawLose())
				.build());

			gsbRoundHistories.add(GsbRoundHistory.builder()
				.gameParticipant(participants.get(secondPlayer.getNickname()))
				.gsbRound(gsbRound)
				.gold(secondPlayer.getHistories().get(round).getGold())
				.silver(secondPlayer.getHistories().get(round).getSilver())
				.bronze(secondPlayer.getHistories().get(round).getBronze())
				.winDrawLose(secondPlayer.getHistories().get(round).getWinDrawLose())
				.build());
		}

		return gsbRoundHistories;
	}

	public RankDTO generateRankDTO(Gsb gsb) {
		List<Gsb.Result> results = gsb.getResults();
		List<String> rankList = new ArrayList<>();

		for(Gsb.Result result : results) {
			if(result.getWinDrawLose().equals(WinDrawLose.WIN)) {
				rankList.add(result.getNickname());
			}
		}
		for(Gsb.Result result : results) {
			if(result.getWinDrawLose().equals(WinDrawLose.LOSE)) {
				rankList.add(result.getNickname());
			}
		}

		return RankDTO.builder()
			.gameCode(gsb.getGameCode())
			.rankList(rankList)
			.build();
	}
}
