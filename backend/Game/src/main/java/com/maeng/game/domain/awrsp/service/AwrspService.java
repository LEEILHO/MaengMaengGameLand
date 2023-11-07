package com.maeng.game.domain.awrsp.service;

import com.maeng.game.domain.awrsp.dto.CardDTO;
import com.maeng.game.domain.awrsp.dto.ResultDTO;
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
            HashMap<Integer, History> history = new HashMap<>();
            history.put(1, History.builder()
                    .card(null)
                    .win(0)
                    .draw(0)
                    .submitAt(null)
                    .build());

            players.put(player.getNickname(), Player.builder()
                            .nickname(player.getNickname())
                            .tier(player.getTier())
                            .profileUrl(player.getProfileUrl())
                            .finish(false)
                            .finishedAt(null)
                            .histories(history)
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
        // 모든 참가자에게서 해당 요청을 받으면 게임 시작 -> 타이머 시작  / 문제 카드 전송
        Game game = getCurrentGame(gameCode);
        game.setCurrentRound(1); // 게임 시작이므로 1라운드로 변경
        awrspRepository.save(game);

        // Submit 생성 : 값이 안들어가면 저장 안됨 -> 카드 제출할 때 없으면 생성해야 될 듯
//        submitRepository.save(Submit.builder().gameCode(gameCode).submit(new HashSet<>()).build());

        MessageDTO messageDTO = MessageDTO.builder()
                .type("AWRSP_CARD")
                .data(CardDTO.builder().problem(game.getProblem()).build())
                .build();

        template.convertAndSend(GAME_EXCHANGE, "awrsp."+gameCode, messageDTO); // 정답 카드 공개
    }

    @Operation(summary = "카드 제출")
    public boolean submitCard(String gameCode, SubmitDTO submitDTO){
        Submit submit = this.getCurrentSubmit(gameCode);

        Game game = this.getCurrentGame(gameCode);
        Player player = game.getPlayers().get(submitDTO.getNickname());
        HashMap<Integer, History> histories = player.getHistories();

        histories.put(game.getCurrentRound(),
                History.builder()
                        .submitAt(submitDTO.getSubmitAt())
                        .card(submitDTO.getCard())
                        .win(0)
                        .draw(0)
                        .build());
        player.setHistories(histories);
        game.getPlayers().put(submitDTO.getNickname(), player);
        awrspRepository.save(game);

        submit.getSubmit().add(submitDTO.getNickname());
        log.info(submit.toString());
        submitRepository.save(submit);

        return submit.getSubmit().size() == game.getHeadCount();
    }

    @Operation(summary = "모든 플레이어의 승 수 계산")
    public void getWinCount(String gameCode){
        log.info("승 수 계산");
        Game game = this.getCurrentGame(gameCode);
        HashMap<String, Player> players = game.getPlayers();
        List<ResultDTO> resultAll = new ArrayList<>();

        // TODO : 모든 플레이어의 승 수 구하기
        for(Player player : players.values()){
            Card[] cardSet = player.getHistories().get(game.getCurrentRound()).getCard();
            if(cardSet == null){
                player.getHistories().put(game.getCurrentRound(), History.builder()
                        .card(new Card[CARD_COUNT])
                        .win(0)
                        .draw(0)
                        .submitAt(null)
                        .build());

                resultAll.add(ResultDTO.builder()
                        .nickname(player.getNickname())
                        .win(0)
                        .draw(0).build());

                continue;
            }

            History history = player.getHistories().get(game.getCurrentRound());
            for (int i = 0; i < CARD_COUNT; i++) {
                int result = this.rockScissorPaper(game.getProblem()[i], cardSet[i]);
                if (result == 1) {
                    history.setWin(history.getWin() + 1);
                }

                if (result == 0) {
                    history.setDraw(history.getDraw() + 1);
                }
            }

            player.getHistories().put(game.getCurrentRound(), history);
            game.getPlayers().put(player.getNickname(), player);
            awrspRepository.save(game);

            resultAll.add(ResultDTO.builder()
                    .nickname(player.getNickname())
                    .win(history.getWin())
                    .draw(history.getDraw()).build());

        }

        awrspRepository.save(game);

        MessageDTO messageDTO = MessageDTO.builder()
                .type("CARD_RESULT")
                .data(resultAll)
                .build();

        template.convertAndSend(GAME_EXCHANGE, "awrsp."+gameCode, messageDTO); // 정답 카드 공개
    }

    public int rockScissorPaper(Card problem, Card card){
        // TODO : 가위바위보 이기면 1, 비기면 0, 지면 -1 반환
        if(problem.equals(Card.PAPER)){
            if(card.equals(Card.SCISSOR)){
                return 1;
            }

            if(card.equals(Card.DRAW_PAPER)){
                return 0;
            }
        }

        if(problem.equals(Card.ROCK)){
            if(card.equals(Card.PAPER)){
                return 1;
            }

            if(card.equals(Card.DRAW_ROCK)){
                return 0;
            }
        }

        if(problem.equals(Card.SCISSOR)){
            if(card.equals(Card.ROCK)) {
                return 1;
            }

            if(card.equals(Card.DRAW_SCISSOR)){
                return 0;
            }
        }
        return -1;
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

    public Submit getCurrentSubmit(String gameCode){
        Submit submit = submitRepository.findByGameCode(gameCode);

        if(submit == null){
            return Submit.builder().gameCode(gameCode).submit(new HashSet<>()).build();
        }

        return submit;
    }
}
