package com.maeng.game.domain.gsb.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.maeng.game.domain.gsb.dto.*;
import com.maeng.game.domain.gsb.entity.*;
import com.maeng.game.domain.gsb.repository.GsbRepository;
import com.maeng.game.domain.room.dto.GameStartDTO;
import com.maeng.game.domain.room.dto.MessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class GsbService {

    private final GsbRepository gsbRepository;
    private static final int GOLD_WEIGHT = 3;
    private static final int SILVER_WEIGHT = 2;
    private static final int BRONZE_Weight = 1;

    public boolean gameSetting(GameStartDTO gameStartDTO){
        log.info("gameSetting() , gameCode = {}",gameStartDTO.getGameCode());
        StartCard[] cards = new StartCard[2];
        for (int i = 0; i < 2; i++) {
            cards[i] = StartCard.builder().seq(i).selected(false).build();
        }
        Random random = new Random();
        for (int i = cards.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1); // 0부터 i까지 무작위 인덱스 선택
            // i와 j 위치의 요소 교환
            StartCard temp = cards[i];
            cards[i] = cards[j];
            cards[j] = temp;
        }
        Map<Integer, Player> players = new HashMap<>();
        // 게임 정보 세팅
        Gsb gsb = Gsb.builder()
                .gameCode(gameStartDTO.getGameCode())
                .roomCode(gameStartDTO.getRoomCode())
                .startCards(cards)
                .players(players)
                .participiants(gameStartDTO.getParticipant())
                .build();
        gsbRepository.save(gsb);

        return true;
    }

    public StartCard[] getStartCards(String gameCode){
        log.info("getStartCards(), gameCode = {}", gameCode);
        return getInfo(gameCode).getStartCards();
    }
    @Transactional
    public void setSeq(String gameCode, PlayerSeqDto playerSeqDto){
        log.info("setSeq(), gameCode = {}, playerSeq = {}, playerNick = {}",gameCode,playerSeqDto.getSeq(),playerSeqDto.getNickname());
        Gsb gsb = gsbRepository.findById(gameCode).orElseThrow();

        // seq 존재 하지 않을 때
        StartCard[] startCards = gsb.getStartCards();
        Map<Integer,Player> players = gsb.getPlayers();
        int s = startCards[playerSeqDto.getSeq()].getSeq();
        int seq = playerSeqDto.getSeq() ;
        if(players == null){
            players = new HashMap<>();
        }
        System.out.println(players.containsKey(0));
        if(!startCards[seq].isSelected() && ! players.containsKey(s)) {
            boolean duplicated = false;
            for(StartCard startCard : startCards) {
                if(playerSeqDto.getNickname().equals(startCard.getNickname())){
                    duplicated = true;
                    break;
                }
            }
            // 중복이 아니라면
            if(!duplicated){
                int idx = -1;
                if(gsb.getParticipiants().get(0).getNickname().equals(playerSeqDto.getNickname())){
                    idx = 0;
                } else{
                    idx = 1;
                }

                // 데이터 넣기
                players.put(s, Player.builder().nickname(playerSeqDto.getNickname())
                        .profileUrl(gsb.getParticipiants().get(idx).getProfileUrl())
                                .tier(gsb.getParticipiants().get(idx).getTier()).currentWeight(0)
                        .currentGold(3).currentSilver(10).currentBronze(20).currentChips(40).build());

                // 선택 처리
                startCards[seq].setSelected(true);
                startCards[seq].setNickname(playerSeqDto.getNickname());
            }
            gsb.setPlayers(players);
            gsbRepository.save(gsb);

        }
    }
    @Transactional
    public MessageDTO setStar(String gameCode, StarDto starDto){
        log.info("setStar(), gameCode = {}, gold = {}, silver = {}, bronze = {}", gameCode, starDto.getGold()
                , starDto.getSilver(), starDto.getBronze());
        MessageDTO messageDTO = null;
        Gsb gsb = getInfo(gameCode);
        String curPlayer = gsb.getCurrentPlayer();
        int curRound = gsb.getCurrentRound();
        // 짝수 라운드
        if(curRound%2==0){
            log.info("짝수 라운드");
            //1인덱스 플레이어가 선 플레이어
            if(curPlayer.equals(gsb.getPlayers().get(1).getNickname())){
                log.info("선 플레이어");
                // 현재 플레이어가 선 플레이어일 때
                History history = setStarFirst(gameCode, starDto);
                Map<Integer, History>  historyMap = gsb.getPlayers().get(1).getHistories();
                if( history != null){
                    if(historyMap == null ){

                        historyMap = new HashMap<>();

                    }
                    /*현재 플레이어의 무게를 다음 플레이어가 못맞출 때 게임 종료*/
                    // 다음 플레이어의 남음 금은동 수
                    int gold = gsb.getPlayers().get(0).getCurrentGold();
                    int silver = gsb.getPlayers().get(0).getCurrentSilver();
                    int bronze =gsb.getPlayers().get(0).getCurrentBronze();
                    if (getWeight(starDto) -2 > getWeight(gold,silver,bronze)){
                        /*TODO: 게임 종료 로직 + 타입*/
                        log.info("후 플레이어가 무게 맛출 수 없어 게임 종료");

                        return MessageDTO.builder()
                                .type("게임 결과")
                                .data(getGameResult(gameCode))
                                .build();


                    }
                    history.setBettingChips(3);
                    gsb.setCarryOverChips(gsb.getCarryOverChips() + 3);
                    historyMap.put(curRound,history);
                    gsb.getPlayers().get(1).setHistories(historyMap);
                    gsb.getPlayers().get(1).setCurrentGold(gsb.getPlayers().get(1).getCurrentGold()-starDto.getGold());
                    gsb.getPlayers().get(1).setCurrentSilver(gsb.getPlayers().get(1).getCurrentSilver()-starDto.getSilver());
                    gsb.getPlayers().get(1).setCurrentBronze(gsb.getPlayers().get(1).getCurrentBronze()-starDto.getBronze());
                    gsb.getPlayers().get(1).setCurrentChips(gsb.getPlayers().get(1).getCurrentChips() - 3);
                    messageDTO = MessageDTO.builder()
                            .type("다음 플레이어 별 세팅")
                            .data(StarResponseDto.builder()
                                    .currentPlayer(gsb.getCurrentPlayer())
                                    .timer(30)
                                    .weight(gsb.getPlayers().get(1).getHistories().get(curRound).getWeight())
                                    .nextPlayer(gsb.getPlayers().get(0).getNickname())
                                    .build())
                            .build();
                    gsb.setCurrentPlayer(gsb.getPlayers().get(0).getNickname());

                }
            } else{
                // 현재 플레이어가 후 플레이어일 때
                log.info("후 플레이어");
                History history = setStarSecond(curRound,0,gameCode, starDto);
                Map<Integer, History>  historyMap = gsb.getPlayers().get(0).getHistories();

                if( history != null){
                    if(historyMap == null ){

                        historyMap = new HashMap<>();

                    }
                    history.setBettingChips(3);
                    gsb.setCarryOverChips(gsb.getCarryOverChips() + 3);
                    historyMap.put(curRound,history);
                    gsb.getPlayers().get(0).setHistories(historyMap);
                    gsb.getPlayers().get(0).setCurrentGold(gsb.getPlayers().get(0).getCurrentGold()-starDto.getGold());
                    gsb.getPlayers().get(0).setCurrentSilver(gsb.getPlayers().get(0).getCurrentSilver()-starDto.getSilver());
                    gsb.getPlayers().get(0).setCurrentBronze(gsb.getPlayers().get(0).getCurrentBronze()-starDto.getBronze());
                    gsb.getPlayers().get(0).setCurrentChips(gsb.getPlayers().get(0).getCurrentChips() - 3);

                    messageDTO = MessageDTO.builder()
                            .type("다음 플레이어 베팅 시작")
                            .data(StarResponseDto.builder()
                                    .currentPlayer(gsb.getCurrentPlayer())
                                    .timer(30)
                                    .weight(gsb.getPlayers().get(0).getHistories().get(curRound).getWeight())
                                    .nextPlayer(gsb.getPlayers().get(1).getNickname())
                                    .build())
                            .build();
                    gsb.setCurrentPlayer(gsb.getPlayers().get(1).getNickname());

                }

            }
        } else{
            log.info("홀수 라운드");
            // 홀수 라운드
            //0 인덱스 플레이어가 선 플레이어
            if(curPlayer.equals(gsb.getPlayers().get(0).getNickname())){
                log.info("선플레이어 ");

                // 현재 플레이어가 선 플레이어일 때
                History history = setStarFirst(gameCode, starDto);
                Map<Integer, History>  historyMap = gsb.getPlayers().get(0).getHistories();
                if( history != null){
                    if(historyMap == null ){

                        historyMap = new HashMap<>();

                    }
                    int gold = gsb.getPlayers().get(1).getCurrentGold();
                    int silver = gsb.getPlayers().get(1).getCurrentSilver();
                    int bronze =gsb.getPlayers().get(1).getCurrentBronze();
                    if (getWeight(starDto) -2 > getWeight(gold,silver,bronze)){
                        /*TODO: 게임 종료 로직 + 타입*/
                        log.info("후 플레이어가 무게 맛출 수 없어 게임 종료");
                        return MessageDTO.builder()
                                .type("게임 결과")
                                .data(getGameResult(gameCode))
                                .build();

                    }
                    history.setBettingChips(3);
                    gsb.setCarryOverChips(gsb.getCarryOverChips() + 3);
                    historyMap.put(curRound,history);
                    gsb.getPlayers().get(0).setHistories(historyMap);
                    gsb.getPlayers().get(0).setCurrentGold(gsb.getPlayers().get(0).getCurrentGold()-starDto.getGold());
                    gsb.getPlayers().get(0).setCurrentSilver(gsb.getPlayers().get(0).getCurrentSilver()-starDto.getSilver());
                    gsb.getPlayers().get(0).setCurrentBronze(gsb.getPlayers().get(0).getCurrentBronze()-starDto.getBronze());
                    gsb.getPlayers().get(0).setCurrentChips(gsb.getPlayers().get(0).getCurrentChips() - 3);

                    messageDTO = MessageDTO.builder()
                            .type("다음 플레이어 별 세팅")
                            .data(StarResponseDto.builder()
                                    .currentPlayer(gsb.getCurrentPlayer())
                                    .timer(30)
                                    .weight(gsb.getPlayers().get(0).getHistories().get(curRound).getWeight())
                                    .nextPlayer(gsb.getPlayers().get(1).getNickname())
                                    .build())
                            .build();
                    gsb.setCurrentPlayer(gsb.getPlayers().get(1).getNickname());
                }
            } else{
                log.info("후플레이어");
                History history = setStarSecond(curRound,1, gameCode, starDto);
                Map<Integer, History>  historyMap = gsb.getPlayers().get(1).getHistories();
                if( history != null){
                    if(historyMap == null ){
                        historyMap = new HashMap<>();

                    }
                    history.setBettingChips(3);
                    gsb.setCarryOverChips(gsb.getCarryOverChips() + 3);
                    historyMap.put(curRound,history);
                    gsb.getPlayers().get(1).setHistories(historyMap);
                    gsb.getPlayers().get(1).setCurrentGold(gsb.getPlayers().get(1).getCurrentGold()-starDto.getGold());
                    gsb.getPlayers().get(1).setCurrentSilver(gsb.getPlayers().get(1).getCurrentSilver()-starDto.getSilver());
                    gsb.getPlayers().get(1).setCurrentBronze(gsb.getPlayers().get(1).getCurrentBronze()-starDto.getBronze());
                    gsb.getPlayers().get(1).setCurrentChips(gsb.getPlayers().get(1).getCurrentChips() - 3);

                    messageDTO = MessageDTO.builder()
                            .type("다음 플레이어 베팅 시작")
                            .data(StarResponseDto.builder()
                                    .currentPlayer(gsb.getCurrentPlayer())
                                    .timer(30)
                                    .weight(gsb.getPlayers().get(1).getHistories().get(curRound).getWeight())
                                    .nextPlayer(gsb.getPlayers().get(0).getNickname())
                                    .build())
                            .build();
                    gsb.setCurrentPlayer(gsb.getPlayers().get(0).getNickname());

                }
            }
        }
        gsbRepository.save(gsb);
        return messageDTO;

    }

    // 선 플레이어 일때
    public History setStarFirst(String gameCode, StarDto starDto){
        log.info("선 플레이어 세팅");
        int weight = getWeight(starDto);
        if(weight<4 || weight >12){
            // TODO: 들어올 수 없는 값 예외 처리
            log.info("들어 올 수 없는 값");
            return null;
        }
        History history = History.builder()
                .bronze(starDto.getBronze())
                .silver(starDto.getSilver())
                .gold(starDto.getGold())
                .weight(weight)
                .build();

        return history;
    }
    // 후 플레이어 일 때
    public History setStarSecond(int round,int idx, String gameCode, StarDto starDto){
        log.info("후 플레이어 세팅");
        int beforeIdx = -1;
        if(idx==0){
            beforeIdx =1;
        } else{
            beforeIdx =0;
        }
        int firstWeight = getInfo(gameCode).getPlayers().get(beforeIdx).getHistories().get(round).getWeight();

        int weight = getWeight(starDto);
        // 차이가 이상 3일 때
        if(Math.abs(firstWeight - weight) >3 ){
            // TODO: 들어올 수 없는 값 예외 처리
            log.info("들어 올 수 없는 값");
            return null;
        }

        History history = History.builder()
                .bronze(starDto.getBronze())
                .silver(starDto.getSilver())
                .gold(starDto.getGold())
                .bettingChips(0)
                .weight(weight)
                .build();
        return history;



    }

    public int getWeight(StarDto starDto){
        return starDto.getGold()*GOLD_WEIGHT
                + starDto.getSilver()*SILVER_WEIGHT
                + starDto.getBronze() *BRONZE_Weight;
    }
    public int getWeight(int gold , int silver, int bronze){
        return  gold*GOLD_WEIGHT+ silver*SILVER_WEIGHT+bronze*BRONZE_Weight;
    }



    public MessageDTO setBet(String gameCode, BettingDto bettingDto){
        log.info("setBet(), gameCode = {}, giveUp = {}, chips = {}", gameCode, bettingDto.isGiveUp(), bettingDto.getBettingChips());
        Gsb gsb = getInfo(gameCode);
        MessageDTO messageDto = null;
        String currentPlayer = gsb.getCurrentPlayer();
        int currentRound = gsb.getCurrentRound();
        int myIdx =-1;
        int nextIdx = -1;
       if(gsb.getPlayers().get(0).getNickname().equals(currentPlayer)){
            myIdx =0;
            nextIdx =1;
        } else {
            nextIdx =0;
            myIdx=1;

        }
        // 베팅 포기
        if(bettingDto.isGiveUp()){
            /*TODO: 베팅 포기 로직 추가*/
            log.info("베팅 포기");
            messageDto =  MessageDTO.builder()
                    .type("베팅 포기")
                    .build();
        }
        else{
            int currentRoundMyChips = gsb.getPlayers().get(myIdx).getHistories().get(currentRound).getBettingChips();
            int currentRoundYourChips = gsb.getPlayers().get(nextIdx).getHistories().get(currentRound).getBettingChips();

            /**
             * 내 배팅이 3 이고 상대 배팅이 3이면 내가 선
             * 상대 남은 칩보다 적거나 같게 배팅할 수 있음
             * 마지막에 내가 낸 칩의 갯수와 상대의 칩의 갯수가 같으면
             * 라운드 종료 로직 호출
             * */
            // 선 플레이어 일 떄
            if(currentRoundMyChips==3 && currentRoundYourChips ==3){
                if( bettingDto.getBettingChips() > gsb.getPlayers().get(myIdx).getCurrentChips()
                        || bettingDto.getBettingChips() > gsb.getPlayers().get(nextIdx).getCurrentChips()){
                    log.info("예외");
                    return null;
                }
            } else{
                // 선 플레이어가 아닐 때
                if(bettingDto.getBettingChips() > gsb.getPlayers().get(myIdx)
                        .getCurrentChips() || bettingDto.getBettingChips() + currentRoundMyChips < currentRoundYourChips){
                    log.info("예외");
                    return null;
                }
            }

            log.info("일반적인 베팅");
            gsb.setCarryOverChips(gsb.getCarryOverChips()+bettingDto.getBettingChips());
            gsb.getPlayers().get(myIdx).setCurrentChips(gsb.getPlayers().get(myIdx).getCurrentChips() - bettingDto.getBettingChips());
            log.info("chips 로그  ={}",gsb.getPlayers().get(myIdx).getCurrentChips());
            gsb.getPlayers().get(myIdx).getHistories().get(currentRound).setBettingChips(currentRoundMyChips+bettingDto.getBettingChips());
            BettingResponseDto betting = null;
            // 다음 플레이어 베팅 해야함.
            if(currentRoundMyChips + bettingDto.getBettingChips() > currentRoundYourChips){
                log.info("다음 플레이어 배팅");
                /* TODO: 베팅 DTO 반환*/
                betting = BettingResponseDto.builder()
                        .timer(30)
                        .nextPlayer(gsb.getPlayers().get(nextIdx).getNickname())
                        .currentPlayer(gsb.getPlayers().get(myIdx).getNickname())
                        .currentChips(bettingDto.getBettingChips())
                        .totalChips(currentRoundMyChips + bettingDto.getBettingChips())
                        .build();
                messageDto = MessageDTO.builder()
                        .type("다음 플레이어 베팅")
                        .data(betting)
                        .build();

                gsb.setCurrentPlayer(gsb.getPlayers().get(nextIdx).getNickname());
            } else if (currentRoundYourChips == currentRoundMyChips+bettingDto.getBettingChips()) {
                log.info("라운드 종료");
                /* TODO: 베팅 DTO 반환*/
                betting = BettingResponseDto.builder()
                        .currentPlayer(gsb.getPlayers().get(myIdx).getNickname())
                        .currentChips(bettingDto.getBettingChips())
                        .totalChips(currentRoundMyChips + bettingDto.getBettingChips())
                        .build();

                messageDto = MessageDTO.builder()
                        .type("라운드 종료")
                        .data(betting)
                        .build();



            } else {
                log.info("에러");
            }
        }

        gsbRepository.save(gsb);
        return  messageDto;



    }
    public boolean endRound(String gameCode){
        log.info("endRound(), gameCode = {}",gameCode);
        Gsb gsb = getInfo(gameCode);
        int currentRound = gsb.getCurrentRound();
        if((gsb.getPlayers().get(0).getHistories().get(currentRound)==null &&
                gsb.getPlayers().get(1).getHistories().get(currentRound) == null)
                                        ||gsb.getPlayers().get(0).getHistories().get(currentRound).getBettingChips()==
                gsb.getPlayers().get(1).getHistories().get(currentRound).getBettingChips()){
            return true;
        }

        return false;
    }
    public MessageDTO getRoundResult(String gameCode){
        log.info("getRoundResult() , gameCode = {}", gameCode);
        Gsb gsb = getInfo(gameCode);
        int currentRound = gsb.getCurrentRound();
        Player player1 = gsb.getPlayers().get(0);
        Player player2 = gsb.getPlayers().get(1);
        History player1History = player1.getHistories().get(currentRound);
        History player2History = player2.getHistories().get(currentRound);
        int player1Gold = player1History.getGold();
        int player1Silver = player1History.getSilver();
        int player1Bronze = player1History.getBronze();
        int player2Gold = player2History.getGold();
        int player2Silver = player2History.getSilver();
        int player2Bronze = player2History.getBronze();
        int winner = -1;
        if(player1Gold>player2Gold){
            //1 승리
            winner = 0;
        } else if(player1Gold<player2Gold){
            //2 승리
            winner = 1;
        } else{
            if(player1Silver> player2Silver){
                //1 승리
                winner = 0;
            } else if(player1Silver < player2Silver){
                // 2승리
                winner = 1;
            } else{
                if(player1Bronze > player2Bronze){
                    //1 승리
                    winner = 0;
                } else if(player1Bronze < player2Bronze ){
                    //2 승리
                    winner = 1;
                }
            }
        }




        MessageDTO messageDTO;
        String nextPlayer;
        // 다음 라운드가 짝수
        if((currentRound+1) %2 ==0){
            nextPlayer = player2.getNickname();
        } else{
            nextPlayer = player1.getNickname();
        }

        if(winner==0){
            log.info("1번 플레이어 승리");
            player1.setCurrentChips(player1.getCurrentChips() + gsb.getCarryOverChips());
            player1History.setChipsChange(gsb.getCarryOverChips() - player1History.getBettingChips());
            player2History.setChipsChange(-player2History.getBettingChips());
            player1History.setWinDrawLose(WinDrawLose.WIN);
            player2History.setWinDrawLose(WinDrawLose.LOSE);
            gsb.setCarryOverChips(0);
            messageDTO = MessageDTO.builder()
                    .type("라운드 결과")
                    .data(RoundResultResponseDto.builder()
                            .currentRound(currentRound)
                            .nextRound(currentRound+1)
                            .nextPlayer(nextPlayer)
                            .timer(30)
                            .draw(false)
                            .winner(player1.getNickname())
                            .winnerGold(player1Gold)
                            .winnerSilver(player1Silver)
                            .winnerBronze(player1Bronze)
                            .currentWinnerChips(player1.getCurrentChips())
                            .loser(player2.getNickname())
                            .loserGold(player2Gold)
                            .loserSilver(player2Silver)
                            .loserBronze(player2Bronze)
                            .currentLoserChips(player2.getCurrentChips())
                            .build())
                    .build();

        } else if(winner==1){
            log.info("2번 플레이어 승리");
            /*TODO: 전 라운드가 비겼는지 확인*/
            player2.setCurrentChips(player2.getCurrentChips() + gsb.getCarryOverChips());
            player1History.setChipsChange(-player1History.getBettingChips());
            player2History.setChipsChange(gsb.getCarryOverChips()-player2History.getBettingChips());
            player1History.setWinDrawLose(WinDrawLose.LOSE);
            player2History.setWinDrawLose(WinDrawLose.WIN);
            gsb.setCarryOverChips(0);
            messageDTO = MessageDTO.builder()
                    .type("라운드 결과")
                    .data(RoundResultResponseDto.builder()
                            .currentRound(currentRound)
                            .nextRound(currentRound+1)
                            .nextPlayer(nextPlayer)
                            .timer(30)
                            .draw(false)
                            .winner(player2.getNickname())
                            .winnerGold(player2Gold)
                            .winnerSilver(player2Silver)
                            .winnerBronze(player2Bronze)
                            .currentWinnerChips(player2.getCurrentChips())
                            .loser(player1.getNickname())
                            .loserGold(player1Gold)
                            .loserSilver(player1Silver)
                            .loserBronze(player1Bronze)
                            .currentLoserChips(player1.getCurrentChips())
                            .build())
                    .build();

        } else{
            log.info("비김");
            player1History.setWinDrawLose(WinDrawLose.DRAW);
            player1History.setChipsChange(-player1History.getBettingChips());
            player2History.setWinDrawLose(WinDrawLose.DRAW);
            player2History.setChipsChange(-player2History.getBettingChips());

            messageDTO = MessageDTO.builder()
                    .type("라운드 결과 비김")
                    .data(RoundDrawResultResponseDto.builder()
                            .timer(30)
                            .currentRound(currentRound)
                            .nextRound(currentRound+1)
                            .nextPlayer(nextPlayer)
                            .draw(true)
                            .player1(player1.getNickname())
                            .currentPlayer1Chips(player1.getCurrentChips())
                            .player2(player2.getNickname())
                            .currentPlayer2Chips(player2.getCurrentChips())
                            .build())
                    .build();

        }
        // 다음 라운드
        gsb.nextRound();
        gsb.setCurrentPlayer(nextPlayer);
        gsbRepository.save(gsb);
        return messageDTO;

    }

    public MessageDTO getGiveUpRoundResult(String gameCode){
        log.info("getGiveUpRoundResult() gameCode = {}" , gameCode);
        MessageDTO messageDTO = null;
        Gsb gsb = getInfo(gameCode);
        int currentRound = gsb.getCurrentRound();
        String giveUpPlayer = gsb.getCurrentPlayer();
        log.info("베팅 포기한 플레이어 = {}",giveUpPlayer);
        Player winnerPlayer;
        Player loserPlayer;
        if(gsb.getPlayers().get(0).getNickname().equals(giveUpPlayer)){
            winnerPlayer = gsb.getPlayers().get(1);
            loserPlayer = gsb.getPlayers().get(0);
        } else{
            winnerPlayer = gsb.getPlayers().get(0);
            loserPlayer = gsb.getPlayers().get(1);
        }
        String nextPlayer;
        // 다음 라운드가 짝수
        if((currentRound+1) %2 ==0){
            nextPlayer = gsb.getPlayers().get(1).getNickname();
        } else{
            nextPlayer = gsb.getPlayers().get(0).getNickname();
        }

        History winnerHistory = winnerPlayer.getHistories().get(currentRound);
        History loserHistory = loserPlayer.getHistories().get(currentRound);
        winnerPlayer.setCurrentChips(winnerPlayer.getCurrentChips() +gsb.getCarryOverChips());
        winnerHistory.setChipsChange(gsb.getCarryOverChips() - winnerHistory.getBettingChips());
        loserHistory.setChipsChange(-loserHistory.getBettingChips());
        winnerHistory.setWinDrawLose(WinDrawLose.LOSE);
        loserHistory.setWinDrawLose(WinDrawLose.WIN);
        gsb.setCarryOverChips(0);
        messageDTO = MessageDTO.
                builder()
                .type("게임 포기 라운드 결과")
                .data(GiveUpResponseDto.builder()
                        .nextRound(gsb.getCurrentRound())
                        .nextPlayer(nextPlayer)
                        .timer(30)
                        .winner(winnerPlayer.getNickname())
                        .currentWinnerChips(winnerPlayer.getCurrentChips())
                        .loser(loserPlayer.getNickname())
                        .currentLoserChips(loserPlayer.getCurrentChips())
                        .build())
                .build();

        // 다음 라운드
        gsb.nextRound();
        gsb.setCurrentPlayer(nextPlayer);
        gsbRepository.save(gsb);
        return messageDTO;

    }
    public MessageDTO getEndGame(String gameCode){
        return MessageDTO.builder()
                    .type("게임 결과")
                    .data(getGameResult(gameCode))
                    .build();
    }

    // 라운드가 종료되었을 때 종료 조건 체크
    public boolean endGame(String gameCode){
        Gsb gsb = getInfo(gameCode);
        boolean end = false;
        Player player1;
        Player player2;
        // 선 플레이어1 후플레이어2
        if (gsb.getCurrentRound()%2==0){
            player1 = gsb.getPlayers().get(1);
            player2 = gsb.getPlayers().get(0);
        } else {
            player1 = gsb.getPlayers().get(0);
            player2 = gsb.getPlayers().get(1);
        }
        // 선플레이어의 칩 개수, 선 플레이어의 낼 수 있는 무게 확인.
        if (player1.getCurrentChips()<3 || getWeight(player1.getCurrentGold(), player1.getCurrentSilver(), player1.getCurrentBronze())<4){
            end = true;
        }
        // 후 플레이어의 칩 개수 확인
        if(player2.getCurrentChips()<3){
            end = true;
        }
        return  end;
    }
    public GameResultDto getGameResult(String gameCode){
        GameResultDto gameResultDto;
        Gsb gsb = getInfo(gameCode);
        Player player1 = gsb.getPlayers().get(0);
        Player player2 = gsb.getPlayers().get(1);
        if (gsb.getCarryOverChips() !=0){
            player1.setCurrentChips(player1.getCurrentChips()+ gsb.getCarryOverChips()/2);
            player2.setCurrentChips(player2.getCurrentChips()+ gsb.getCarryOverChips()/2);
        }
        int coin1 = getWeight(player1.getCurrentGold(),player1.getCurrentSilver(),player1.getCurrentBronze());
        int coin2 = getWeight(player2.getCurrentGold(),player2.getCurrentSilver(),player2.getCurrentBronze());
        player1.setCurrentChips(player1.getCurrentChips() + coin1);
        player2.setCurrentChips(player2.getCurrentChips() + coin2);
        if (player1.getCurrentChips()> player2.getCurrentChips()){
            gameResultDto = GameResultDto.builder()
                    .draw(false)
                    .winner(player1.getNickname())
                    .winnerChips(player1.getCurrentChips())
                    .loser(player2.getNickname())
                    .loserChips(player2.getCurrentChips())
                    .build();
        } else if (player1.getCurrentChips()< player2.getCurrentChips()){
            gameResultDto = GameResultDto.builder()
                    .draw(false)
                    .winner(player2.getNickname())
                    .winnerChips(player2.getCurrentChips())
                    .loser(player1.getNickname())
                    .loserChips(player1.getCurrentChips())
                    .build();
        } else {
            gameResultDto = GameResultDto.builder()
                    .draw(true)
                    .winner(player1.getNickname())
                    .winnerChips(player1.getCurrentChips())
                    .loser(player2.getNickname())
                    .loserChips(player2.getCurrentChips())
                    .build();
        }
        gsbRepository.save(gsb);
        return gameResultDto;
    }
    public MessageDTO getEndTimer(String gameCode, GsbNicknameDto nicknameDto){
        Gsb gsb = getInfo(gameCode);
        if(nicknameDto.getNickname().equals(gsb.getPlayers().get(0).getNickname())){
            return MessageDTO.builder()
                    .type("타이머 게임 종료")
                    .data(TimerEndDto.builder()
                            .winner(gsb.getPlayers().get(1).getNickname())
                            .loser(gsb.getPlayers().get(0).getNickname())
                            .build())
                    .build();
        } else {
            return MessageDTO.builder()
                    .type("타이머 게임 종료")
                    .data(TimerEndDto.builder()
                            .winner(gsb.getPlayers().get(0).getNickname())
                            .loser(gsb.getPlayers().get(1).getNickname())
                            .build())
                    .build();

        }

    }




    public Gsb setGsb(String gameCode) {
        Gsb gsb = gsbRepository.findById(gameCode).orElseThrow();
        gsb.nextRound();
        gsb.setCurrentPlayer(gsb.getPlayers().get(0).getNickname());
        gsbRepository.save(gsb);

        return gsb;

    }


    public Gsb getInfo(String gameCode){
        return gsbRepository.findById(gameCode).orElseThrow();

    }
    public String getDataToJson(String gameCode) {
        Gsb gsb = gsbRepository.findById(gameCode).orElseThrow();
        String json = "";
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            json = objectMapper.writeValueAsString(gsb);
        } catch (Exception e){
            log.error("json error : {} ", e.getMessage());
        }
        return json;
    }

}
