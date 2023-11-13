package com.maeng.record.domain.record.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.maeng.record.domain.record.data.Jwac;
import com.maeng.record.domain.record.dto.RankDTO;
import com.maeng.record.domain.record.entity.Game;
import com.maeng.record.domain.record.entity.GameParticipant;
import com.maeng.record.domain.record.entity.GameUser;
import com.maeng.record.domain.record.entity.JwacRound;
import com.maeng.record.domain.record.entity.JwacRoundBid;
import com.maeng.record.domain.record.enums.GameCategoty;
import com.maeng.record.domain.record.enums.Jewelry;
import com.maeng.record.domain.record.exception.GameAlreadyExistException;
import com.maeng.record.domain.record.repository.GameParticipantRepository;
import com.maeng.record.domain.record.repository.GameRepository;
import com.maeng.record.domain.record.repository.GameUserRepository;
import com.maeng.record.domain.record.repository.JwacBidRepository;
import com.maeng.record.domain.record.repository.JwacRoundRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwacRecordService {
	private final GameRepository gameRepository;
	private final JwacBidRepository jwacBidRepository;
	private final GameUserRepository gameUserRepository;
	private final JwacRoundRepository jwacRoundRepository;
	private final GameParticipantRepository gameParticipantRepository;

	public void saveJwacRecord(Jwac jwac) {
		Game game = createGame(jwac.getGameCode(), jwac.getCreateAt());

		Map<String, GameParticipant> participants = createGameParticipant(game, new ArrayList<>(jwac.getPlayers().keySet()), jwac);

		Map<Integer, JwacRound> jwacRounds = createJwacRound(game, jwac.getMaxRound(), jwac.getJewelry());

		List<JwacRoundBid> jwacRoundBids = createJwacBid(jwac.getPlayers(), participants, jwacRounds);

		gameRepository.save(game);
		gameParticipantRepository.saveAll(participants.values());
		jwacRoundRepository.saveAll(jwacRounds.values());
		jwacBidRepository.saveAll(jwacRoundBids);
	}

	private Game createGame(String gameCode, LocalDateTime startAt) {
		if(gameRepository.existsById(gameCode)) {
			throw new GameAlreadyExistException(gameCode);
		}
		return Game.builder()
				.gameCode(gameCode)
				.gameCategory(GameCategoty.JWERLY_AUCTION)
				.startAt(startAt)
				.build();
	}

	private Map<String, GameParticipant> createGameParticipant(Game game, List<String> participants, Jwac jwac) {
		List<GameParticipant> gameParticipants = new ArrayList<>();
		for(String participant : participants) {
			GameUser gameUser = getOrCreateUser(participant);
			int score = jwac.getPlayers().get(participant).getScore();
			int rank = jwac.getRank().indexOf(participant) + 1;
			GameParticipant gameParticipant = GameParticipant.builder()
				.game(game)
				.gameUser(gameUser)
				.score(score)
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

	private Map<Integer, JwacRound> createJwacRound(Game game, Integer maxRound, Map<Integer, Jewelry> jewelry) {
		Map<Integer, JwacRound> jwacRounds = new HashMap<>();
		for(int round = 1; round <= maxRound; round++) {
			JwacRound jwacRound = JwacRound.builder()
				.game(game)
				.round(round)
				.jewelry(jewelry.get(round))
				.build();

			jwacRounds.put(round, jwacRound);
		}

		return jwacRounds;
	}

	private List<JwacRoundBid> createJwacBid(Map<String, Jwac.Player> players, Map<String, GameParticipant> participants, Map<Integer, JwacRound> jwacRounds) {
		List<JwacRoundBid> jwacRoundBids = new ArrayList<>();

		for(int round = 1; round <= jwacRounds.size(); round++) {
			JwacRound jwacRound = jwacRounds.get(round);

			GameParticipant maxBidder = null;
			long maxBidAmount = 0L;

			for(String player : players.keySet()) {
				Jwac.Player playerInfo = players.get(player);
				GameParticipant gameParticipant = participants.get(player);

				if(playerInfo.getHistory() == null) {
					continue;
				}

				if (playerInfo.getHistory().containsKey(round)) {
					Jwac.History history = playerInfo.getHistory().get(round);

					jwacRoundBids.add(JwacRoundBid.builder()
						.jwacRound(jwacRound)
						.gameUser(gameParticipant.getGameUser())
						.bidAt(history.getBidAt())
						.bidAmount(history.getBidAmount())
						.build());

					if (maxBidAmount < history.getBidAmount()) {
						maxBidder = gameParticipant;
						maxBidAmount = history.getBidAmount();
					}
				}
			}

			jwacRound.update(JwacRound.builder()
				.gameParticipant(maxBidder)
				.build());
		}

		return jwacRoundBids;
	}

	public RankDTO generateRankDTO(Jwac jwac) {
		return RankDTO.builder()
			.gameCode(jwac.getGameCode())
			.rankList(jwac.getRank())
			.build();
	}
}
