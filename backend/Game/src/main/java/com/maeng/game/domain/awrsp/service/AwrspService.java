package com.maeng.game.domain.awrsp.service;

import com.maeng.game.domain.awrsp.dto.CardDTO;
import com.maeng.game.domain.awrsp.dto.SubmitDTO;
import com.maeng.game.domain.awrsp.entity.*;
import com.maeng.game.domain.awrsp.exception.NotFoundGameException;
import com.maeng.game.domain.awrsp.repository.AwrspRepository;
import com.maeng.game.domain.awrsp.repository.SubmitRepository;
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
    private final SubmitRepository submitRepository;
    private final RabbitTemplate template;
    private static final String GAME_EXCHANGE = "game";
    private static final int CARD_COUNT = 7;
    private static final Card[] cards = { Card.ROCK, Card.ROCK, Card.ROCK,
                                            Card.SCISSOR, Card.SCISSOR, Card.SCISSOR,
                                            Card.PAPER, Card.PAPER, Card.PAPER};

    @Operation(summary = "게임 정보 세팅")
    public boolean gameSetting(GameStartDTO gameStartDTO){
        log.info(gameStartDTO.getGameCode());

        // 플레이어 초기화
        HashMap<String, Player> players = new HashMap<>();
        for(User player : gameStartDTO.getParticipant()){
            players.put(player.getNickname(), Player.builder()
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

        return true;
    }


    @Operation(summary = "게임 참가")
    public void gameStart(String gameCode){
        // TODO : 모든 참가자에게서 해당 요청을 받으면 게임 시작 -> 타이머 시작  / 문제 카드 전송
        Game game = getCurrentGame(gameCode);
        game.setCurrentRound(1); // 게임 시작이므로 1라운드로 변경
        awrspRepository.save(game);

        // TODO : Submit 생성
        submitRepository.save(Submit.builder().gameCode(gameCode).submit(new HashSet<>()).build());

        MessageDTO messageDTO = MessageDTO.builder()
                .type("AWRSP_CARD")
                .data(CardDTO.builder().problem(game.getProblem()).build())
                .build();

        template.convertAndSend(GAME_EXCHANGE, "awrsp."+gameCode, messageDTO); // 정답 카드 공개
    }

    @Operation(summary = "카드 제출")
    public boolean submitCard(String gameCode, SubmitDTO submitDTO){
        // TODO : 해당 라운드에 플레이어의 제출 카드 저장
        Game game = this.getCurrentGame(gameCode);
        HashMap<String, Player> players = game.getPlayers();
        Player player = players.get(submitDTO.getNickname());
        HashMap<Integer, History> history = player.getHistories();

        history.put(game.getCurrentRound(), History.builder()
                        .card(submitDTO.getCard())
                        .submitAt(submitDTO.getSubmitAt())
                .build());

        player.setHistories(history);
        players.put(player.getNickname(), player);
        game.setPlayers(players);
        awrspRepository.save(game);

        // TODO : 모든 플레이어가 제출했는지 확인 후 타이머 끝내고 다음 요청
        Set<String> submit = submitRepository.findByGameCode(gameCode).getSubmit();
        return submit.size() == game.getHeadCount();
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


    public Game getCurrentGame(String gameCode){
        Game game = awrspRepository.findById(gameCode).orElse(null);

        if(game == null){
            throw new NotFoundGameException("게임 정보가 존재하지 않습니다.");
        }

        return game;
    }

}
