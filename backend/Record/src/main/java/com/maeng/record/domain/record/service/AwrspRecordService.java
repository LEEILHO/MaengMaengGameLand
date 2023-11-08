package com.maeng.record.domain.record.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.maeng.record.domain.record.data.Awrsp;
import com.maeng.record.domain.record.entity.AwrspGameAnswer;
import com.maeng.record.domain.record.entity.AwrspRound;
import com.maeng.record.domain.record.entity.AwrspRoundData;
import com.maeng.record.domain.record.entity.Game;
import com.maeng.record.domain.record.entity.GameUser;
import com.maeng.record.domain.record.enums.Card;
import com.maeng.record.domain.record.enums.GameCategoty;
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

	public void saveAwrspRecord(Awrsp awrsp) {
		Game game = createGame(awrsp.getId(), awrsp.getStartedAt());

		List<AwrspGameAnswer> awrspGameAnswer = createGamaAnswer(game, awrsp.getProblem());

		List<AwrspRound> awrspRounds = createAwrspRound(game, awrsp.getPlayers());

		List<AwrspRoundData> awrspRoundData = createAwrspRoundData(awrspRounds, awrsp.getPlayers());

		gameRepository.save(game);
		awrspGameAnswerRepository.saveAll(awrspGameAnswer);
		awrspRoundRepository.saveAll(awrspRounds);
		awrspRoundDataRepository.saveAll(awrspRoundData);
	}

	private Game createGame(String gameCode, LocalDateTime startAt) {
		return Game.builder()
			.gameCode(gameCode)
			.gameCategory(GameCategoty.JWERLY_AUCTION)
			.startAt(startAt)
			.build();
	}

	private List<AwrspGameAnswer> createGamaAnswer(Game game, Card[] answer) {
		List<AwrspGameAnswer> awrspGameAnswer = new ArrayList<>();
		for(int i = 0; i < answer.length; i++) {
			awrspGameAnswer.add(AwrspGameAnswer.builder()
				.game(game)
				.sequence(i + 1)
				.card(answer[i])
				.build());
		}
		return awrspGameAnswer;
	}

	private List<AwrspRound> createAwrspRound(Game game, HashMap<String, Awrsp.Player> players) {
		List<AwrspRound> awrspRounds = new ArrayList<>();
		for(Awrsp.Player player : players.values()) {
			GameUser gameUser = getOrCreateUser(player.getNickname());

			for(int round = 1; round <= player.getHistories().size(); round++) {
				Awrsp.History history = player.getHistories().get(round);
				awrspRounds.add(AwrspRound.builder()
					.game(game)
					.gameUser(gameUser)
					.round(round)
					.win(history.getWin())
					.draw(history.getDraw())
					.build());
			}
		}
		return awrspRounds;
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

	private List<AwrspRoundData> createAwrspRoundData(List<AwrspRound> awrspRounds, HashMap<String, Awrsp.Player> players) {
		List<AwrspRoundData> awrspRoundData = new ArrayList<>();
		for(AwrspRound awrspRound : awrspRounds) {
			Awrsp.Player player = players.get(awrspRound.getGameUser().getNickname());
			Awrsp.History history = player.getHistories().get(awrspRound.getRound());

			for(int i = 0; i < history.getCard().length; i++) {
				awrspRoundData.add(AwrspRoundData.builder()
					.awrspRound(awrspRound)
					.sequence(i + 1)
					.card(history.getCard()[i])
					.build());
			}
		}
		return awrspRoundData;
	}
}
