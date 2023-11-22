package com.maeng.game.domain.awrsp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.maeng.game.domain.awrsp.dto.*;
import com.maeng.game.domain.awrsp.entity.*;
import com.maeng.game.domain.awrsp.exception.NotFoundGameException;
import com.maeng.game.domain.awrsp.repository.AwrspRepository;
import com.maeng.game.domain.awrsp.repository.RankRepository;
import com.maeng.game.domain.awrsp.repository.SubmitRepository;
import com.maeng.game.domain.room.dto.GameStartDTO;
import com.maeng.game.domain.room.dto.MessageDTO;
import com.maeng.game.domain.room.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;


@Slf4j
@RequiredArgsConstructor
@Service
public class AwrspService {

    private final AwrspRepository awrspRepository;
    private final SubmitRepository submitRepository;
    private final RankRepository rankRepository;
    private final RabbitTemplate template;
    private final static String RECORD_EXCHANGE_NAME = "record";
    private static final String GAME_EXCHANGE = "game";
    private static final int CARD_COUNT = 7;
    private static final int MAX_ROUND = 15;
    private static final Card[] cards = { Card.ROCK, Card.ROCK, Card.ROCK,
                                            Card.SCISSOR, Card.SCISSOR, Card.SCISSOR,
                                            Card.PAPER, Card.PAPER, Card.PAPER};

    @Transactional
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
                            .finishRound(0)
                            .histories(history)
                            .rank(gameStartDTO.getHeadCount())
                    .build());
        }

        // 시연 문제 : 빠 찌 묵 빠 찌 묵 빠
//        Card[] problem = {Card.PAPER, Card.SCISSOR, Card.ROCK, Card.PAPER, Card.SCISSOR, Card.ROCK, Card.PAPER};
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
                            .finishCount(0)
                        .build());

        return true;
    }


    @Transactional
    @Operation(summary = "게임 참가")
    public synchronized void enterGame(String gameCode){
        // 모든 참가자에게서 해당 요청을 받으면 게임 시작 -> 타이머 시작  / 문제 카드 전송
        log.info("모든 플레이어 참가 완료");

        Game game = getCurrentGame(gameCode);
        game.setCurrentRound(game.getCurrentRound()+1); // 게임 시작이므로 1라운드로 변경
        awrspRepository.save(game);

        log.info("문제 카드 : "+ Arrays.toString(game.getProblem()));
    }

    @Transactional
    @Operation(summary = "카드 제출")
    public synchronized boolean submitCard(String gameCode, SubmitDTO submitDTO){
        log.info("플레이어 "+submitDTO.getNickname()+" "+Arrays.toString(submitDTO.getCard()));

        Submit submit = this.getCurrentSubmit(gameCode);
        Game game = this.getCurrentGame(gameCode);
        Player player = game.getPlayers().get(submitDTO.getNickname());
        HashMap<Integer, History> histories = player.getHistories();

        if(submit.getSubmit().contains(submitDTO.getNickname())){
            return submit.getSubmit().size() == game.getHeadCount();
        }

        // 플레이어 히스토리에 제출 카드 저장
        histories.put(game.getCurrentRound(),
                History.builder()
                        .submitAt(LocalDateTime.now())
                        .card(submitDTO.getCard())
                        .win(0)
                        .draw(0)
                        .build());
        player.setHistories(histories);
        game.getPlayers().put(submitDTO.getNickname(), player);
        awrspRepository.save(game);

        // Submit에 제출 등록

        submit.getSubmit().add(submitDTO.getNickname());
        log.info(submit.toString());
        submitRepository.save(submit);

        return submit.getSubmit().size() == (game.getHeadCount() - game.getFinishCount());
    }

    @Transactional
    @Operation(summary = "모든 플레이어의 승 수 계산")
    public void getWinCount(String gameCode){
        Game game = this.getCurrentGame(gameCode);
        Submit submit = this.getCurrentSubmit(gameCode);
        HashMap<String, Player> players = game.getPlayers();
        int currentRound = game.getCurrentRound();

        // 카드를 제출한 플레이어의 승 수 구하기, 승리했으면 Rank에 추가
        for(String nickname : submit.getSubmit()){
            Player player = players.get(nickname);

            Card[] cardSet = player.getHistories().get(currentRound).getCard();
            log.info("제출 카드 : "+Arrays.toString(cardSet));

            History history = player.getHistories().get(currentRound);
            for (int i = 0; i < CARD_COUNT; i++) { // 카드 하나씩 확인하면서 가위바위보 결과 반환
                int result = this.rockScissorPaper(game.getProblem()[i], cardSet[i]);
                if (result == 1) {
                    history.setWin(history.getWin() + 1);
                }

                if (result == 0) {
                    history.setDraw(history.getDraw() + 1);
                }
            }

            // 플레이어가 승리했는지 확인
            if(history.getWin() == CARD_COUNT){ // 전승했으면
                Rank rank = this.getCurrentRank(gameCode); // Rank에 추가
                rank.getRank().add(player.getNickname());
                rankRepository.save(rank);

                player.setRank(rank.getRank().size());
                player.setFinish(true);
                player.setFinishRound(game.getCurrentRound());
                player.setFinishedAt(history.getSubmitAt()); // 일단 제출한 시간으로 끝난 시간 저장
                game.setFinishCount(game.getFinishCount()+1);
            }

            // 결과에 따른 플레이어 정보 업데이트
            player.getHistories().put(currentRound, history);
            game.getPlayers().put(player.getNickname(), player);
            awrspRepository.save(game);
            submitRepository.delete(submit);
        }

        // 라운드 결과 생성 및 전송
        this.sendCurrentRoundResult(gameCode, players, submit, currentRound);
    }

    @Transactional(readOnly = true)
    @Operation(summary = "게임 결과 전송")
    public void sendGameResult(String gameCode){
        Game game = this.getCurrentGame(gameCode);
        List<RankResultDTO> result = new ArrayList<>();

        for(Player player : game.getPlayers().values()){
            result.add(RankResultDTO.builder()
                            .nickname(player.getNickname())
                            .round(player.getFinishRound())
                            .rank(player.getRank())
                    .build());
        }

        result.sort(new Comparator<RankResultDTO>() {
            @Override
            public int compare(RankResultDTO o1, RankResultDTO o2) {
                return o1.getRank() - o2.getRank();
            }
        });

        MessageDTO messageDTO = MessageDTO.builder()
                .type("GAME_OVER")
                .data(GameResultDTO.builder()
                        .result(result)
                        .answer(this.getAnswer(game.getProblem()))
                        .build())
                .build();

        log.info(messageDTO.toString());
        template.convertAndSend(GAME_EXCHANGE, "awrsp."+gameCode, messageDTO);
    }

    @Transactional(readOnly = true)
    @Operation(summary = "게임 종료 확인")
    public boolean checkGameOver(String gameCode){
        Game game = this.getCurrentGame(gameCode);
        Rank rank = this.getCurrentRank(gameCode);
        log.info("승리한 사람 : "+game.getFinishCount());

        // TODO : 승리한 사람 + 연결 끊긴 사람 모두가 finishCount 돼서 게임 종료됨
        return game.getCurrentRound() >= MAX_ROUND || (game.getHeadCount() - game.getFinishCount()) <= 1;
    }

    @Transactional
    @Operation(summary = "라운드 결과 생성 및 전송")
    public void sendCurrentRoundResult(String gameCode, HashMap<String, Player> players, Submit submit, int currentRound){
        List<RoundResultDTO> roundResult = new ArrayList<>();
        Set<String> set = new HashSet<>(submit.getSubmit());

        for(Player player : players.values()){
            RoundDetailDTO roundDetail = null;
            History history = player.getHistories().get(currentRound);
            if(set.contains(player.getNickname())){ // 제출한 플레이어이면
                roundDetail = RoundDetailDTO.builder()
                        .win(history.getWin())
                        .draw(history.getDraw())
                        .build();
            }

            roundResult.add(RoundResultDTO.builder()
                            .nickname(player.getNickname())
                            .rank(player.getRank())
                            .finish(player.isFinish())
                            .detail(roundDetail)
                            .profileUrl(player.getProfileUrl())
                    .build());
        }

        // 결과 전송
        MessageDTO messageDTO = MessageDTO.builder()
                .type("CARD_RESULT")
                .data(roundResult)
                .build();

        template.convertAndSend(GAME_EXCHANGE, "awrsp."+gameCode, messageDTO); // 결곽 공개

    }

    @Operation(summary = "1~2 라운드는 비김 카드 패스")
    public boolean passDrawCard(String gameCode){
        Game game = this.getCurrentGame(gameCode);
        return game.getCurrentRound() < 3;
    }

    @Operation(summary = "라운드 정보")
    public void sendRound(String gameCode){
        Game game = this.getCurrentGame(gameCode);

        log.info("[현재 라운드] : " + game.getCurrentRound());
        MessageDTO messageDTO = MessageDTO.builder()
                .type("ROUND")
                .data(game.getCurrentRound())
                .build();

        template.convertAndSend(GAME_EXCHANGE, "awrsp."+gameCode, messageDTO);
    }

    @Operation(summary = "탈주 플레이어 처리")
    public void disconnectedPlayer(String gameCode, String nickname){
        log.info("[AWRSP] 탈주 플레이어 처리 "+gameCode+" "+nickname);

        Game game = this.getCurrentGame(gameCode);
        Player player = game.getPlayers().get(nickname);

        if(player.isFinish()){ // 이미 게임 끝난 사람은 처리 x
            return;
        }

        player.setFinish(true);
        player.setFinishedAt(LocalDateTime.now());
        player.setRank(game.getHeadCount());
        game.setFinishCount(game.getFinishCount()+1);
        game.getPlayers().put(nickname, player);

        awrspRepository.save(game);
    }

    @Operation(summary = "게임 내역 record-service로 전송")
    public void sendGameResultToRecode(String gameCode){
        Game game = this.getCurrentGame(gameCode);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule()); // 날짜 인식하게 하고
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // 문자열로 만들게
            String json = objectMapper.writeValueAsString(game);
            template.convertAndSend(RECORD_EXCHANGE_NAME, "awrsp."+gameCode, json);

            log.info("게임 결과 record-service에 전송 : "+json);
        } catch (Exception e) {
            log.error("json error : {}", e.getMessage());
        }
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

    @Transactional
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

    // TODO : 정답 카드 생성
    public Card[] getAnswer(Card[] problem){
        Card[] answer = new Card[CARD_COUNT];

        for(int i = 0; i < CARD_COUNT; i++){
            Card c = null;
            if(this.rockScissorPaper(problem[i], Card.PAPER) == 1){
                c = Card.PAPER;
            }

            if(this.rockScissorPaper(problem[i], Card.SCISSOR) == 1){
                c = Card.SCISSOR;
            }

            if(this.rockScissorPaper(problem[i], Card.ROCK) == 1){
                c = Card.ROCK;
            }

            answer[i] = c;
        }

        return answer;
    }

    public Game getCurrentGame(String gameCode){
        Game game = awrspRepository.findById(gameCode).orElse(null);

        if(game == null){
            throw new NotFoundGameException("게임 정보가 존재하지 않습니다.");
        }

        return game;
    }

    public Submit getCurrentSubmit(String gameCode){
        Submit submit = submitRepository.findById(gameCode).orElse(null);

        if(submit == null){
            return Submit.builder().gameCode(gameCode).submit(new ArrayList<>()).build();
        }

        return submit;
    }

    @Transactional
    public Rank getCurrentRank(String gameCode){
        Rank rank = rankRepository.findById(gameCode).orElse(null);

        if(rank == null){
            return Rank.builder().gameCode(gameCode).rank(new ArrayList<>()).build();
        }

        return rank;
    }
}
