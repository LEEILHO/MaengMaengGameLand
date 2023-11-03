package com.maeng.game.domain.awrsp.service;

import com.maeng.game.domain.awrsp.dto.CardDTO;
import com.maeng.game.domain.awrsp.entity.Card;
import com.maeng.game.domain.awrsp.entity.Game;
import com.maeng.game.domain.awrsp.entity.Player;
import com.maeng.game.domain.awrsp.repository.AwrspRepository;
import com.maeng.game.domain.room.dto.GameStartDTO;
import com.maeng.game.domain.room.dto.MessageDTO;
import com.maeng.game.domain.room.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;


@Slf4j
@RequiredArgsConstructor
@Service
public class AwrspService {

    private final AwrspRepository awrspRepository;
    private final RabbitTemplate template;
    private static final String GAME_EXCHANGE = "game";
    private static final int CARD_COUNT = 7;
    private static final Card[] cards = { Card.ROCK, Card.ROCK, Card.ROCK,
                                            Card.SCISSOR, Card.SCISSOR, Card.SCISSOR,
                                            Card.PAPER, Card.PAPER, Card.PAPER};

    @Operation(summary = "게임 정보 세팅")
    public void gameSetting(GameStartDTO gameStartDTO){
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

        Card[] problem = this.generateCard();
        // 게임 정보 세팅 후 저장
        awrspRepository.save(Game.builder()
                                .id(gameStartDTO.getGameCode())
                                .roomCode(gameStartDTO.getRoomCode())
                                .startedAt(LocalDateTime.now())
                                .roundStartedAt(null)
                                .headCount(gameStartDTO.getHeadCount())
                                .currentRound(0)
                                .players(players)
                                .problem(problem)
                        .build());

        MessageDTO messageDTO = MessageDTO.builder()
                .type("AWRSP_CARD")
                .data(CardDTO.builder().problem(problem).build())
                .build();

        // 정답 카드 공개
        template.convertAndSend(GAME_EXCHANGE, "awrsp."+gameStartDTO.getGameCode(), messageDTO);
    }

    public void enterGame(String gameCode){

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
