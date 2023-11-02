package com.maeng.game.domain.awrsp.game.service;

import com.maeng.game.domain.awrsp.game.entity.Card;
import com.maeng.game.domain.awrsp.game.entity.Game;
import com.maeng.game.domain.awrsp.game.entity.Player;
import com.maeng.game.domain.awrsp.game.repository.AwrspRepository;
import com.maeng.game.domain.room.dto.GameStartDTO;
import com.maeng.game.domain.room.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;


@Slf4j
@RequiredArgsConstructor
@Service
public class AwrspService {

    private final AwrspRepository awrspRepository;
    private static final int CARD_COUNT = 7;
    private static final Card[] cards = { Card.ROCK, Card.ROCK, Card.ROCK,
                                            Card.SCISSOR, Card.SCISSOR, Card.SCISSOR,
                                            Card.PAPER, Card.PAPER, Card.PAPER};

    public void gameStart(GameStartDTO gameStartDTO){
        log.info(gameStartDTO.getGameCode());

        // 플레이어 초기화
        List<Player> players = new ArrayList<>();
        for(User player : gameStartDTO.getParticipant()){
            players.add(Player.builder()
                            .nickname(player.getNickname())
                            .tier(player.getTier())
                            .profileUrl(player.getProfileUrl())
                            .finish(false)
                            .finishedAt(null)
                            .histories(null)
                    .build());
        }

        // 게임 정보 세팅 후 저장
        awrspRepository.save(Game.builder()
                                .id(gameStartDTO.getGameCode())
                                .roomCode(gameStartDTO.getRoomCode())
                                .startedAt(LocalDateTime.now())
                                .roundStartedAt(null)
                                .headCount(gameStartDTO.getHeadCount())
                                .currentRound(0)
                                .players(players)
                                .problem(this.generateCard())
                        .build());
    }

    public Card[] generateCard(){
        // cards 9개 중에 7개 뽑기
        Card[] problem = new Card[CARD_COUNT];
        boolean[] visited = new boolean[cards.length];
        Random random = new Random();
        int count = 0;

        while(count < CARD_COUNT){ // 7개 뽑을 때까지 반복
            int randomNum = random.nextInt(cards.length);
            if(visited[randomNum]){
                continue;
            }
            problem[count++] = cards[randomNum];
            visited[randomNum] = true;
        }

        return problem;
    }
}
