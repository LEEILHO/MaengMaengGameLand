package com.maeng.game.domain.gsb.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.maeng.game.domain.gsb.dto.PlayerSeqDto;
import com.maeng.game.domain.gsb.dto.StarDto;
import com.maeng.game.domain.gsb.dto.StarResponseDto;
import com.maeng.game.domain.gsb.entity.Gsb;
import com.maeng.game.domain.gsb.entity.History;
import com.maeng.game.domain.gsb.entity.Player;
import com.maeng.game.domain.gsb.entity.StartCard;
import com.maeng.game.domain.gsb.repository.GsbRepository;
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

    public StartCard[] getStartCards(String gameCode){

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
                .gameCode(gameCode)
                .startCards(cards)
                .players(players)
                .build();
        gsbRepository.save(gsb);

        return cards;
    }
    @Transactional
    public void setSeq(String gameCode, PlayerSeqDto playerSeqDto){
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
        if(!startCards[seq].isSelected() && !players.containsKey(s)) {
            boolean duplicated = false;
            for(StartCard startCard : startCards) {

                if(playerSeqDto.getNickname().equals(startCard.getNickname())){
                    duplicated = true;
                }
            }
            // 중복이 아니라면
            if(!duplicated){
                // 데이터 넣기
                players.put(s, Player.builder().nickname(playerSeqDto.getNickname())
                        .profileUrl("프로필사진").currentGold(3).currentSilver(10).currentBronze(20).currentChips(40).build());

                // 선택 처리
                startCards[seq].setSelected(true);
                startCards[seq].setNickname(playerSeqDto.getNickname());
            }
            gsb.setPlayers(players);
            gsbRepository.save(gsb);

        }
    }
    public MessageDTO setStar(String gameCode, StarDto starDto){
        MessageDTO messageDTO = null;
        Gsb gsb = getInfo(gameCode);
        String curPlayer = gsb.getCurrentPlayer();
        int curRound = gsb.getCurrentRound();
        // 짝수 라운드
        if(curRound%2==0){
            log.info("짝수 라운드");
            //1인덱스 플레이어가 선 플레이어
            if(curPlayer.equals(gsb.getPlayers().get(1).getNickname())){
                // 현재 플레이어가 선 플레이어일 때
                History history = setStarFirst(curRound,1,gameCode, starDto);
                Map<Integer, History>  historyMap = gsb.getPlayers().get(1).getHistories();
                if( history != null){
                    if(historyMap == null ){

                        historyMap = new HashMap<>();

                    }

                    historyMap.put(curRound,history);
                    gsb.getPlayers().get(1).setHistories(historyMap);

                    messageDTO = MessageDTO.builder()
                            .type("다음 플레이어 별 세팅")
                            .data(StarResponseDto.builder()
                                    .weight(gsb.getPlayers().get(1).getHistories().get(curRound).getWeight())
                                    .nextPlayer(gsb.getPlayers().get(0).getNickname())
                                    .build())
                            .build();
                    gsb.setCurrentPlayer(gsb.getPlayers().get(0).getNickname());

                }
            } else{
                // 현재 플레이어가 후 플레이어일 때
                History history = setStarSecond(curRound,0,gameCode, starDto);
                Map<Integer, History>  historyMap = gsb.getPlayers().get(0).getHistories();

                if( history != null){
                    if(historyMap == null ){

                        historyMap = new HashMap<>();

                    }

                    historyMap.put(curRound,history);
                    gsb.getPlayers().get(0).setHistories(historyMap);

                    messageDTO = MessageDTO.builder()
                            .type("다음 플레이어 베팅 시작")
                            .data(StarResponseDto.builder()
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
                History history = setStarFirst(curRound,0,gameCode, starDto);
                Map<Integer, History>  historyMap = gsb.getPlayers().get(0).getHistories();
                if( history != null){
                    if(historyMap == null ){

                        historyMap = new HashMap<>();

                    }

                    historyMap.put(curRound,history);
                    gsb.getPlayers().get(0).setHistories(historyMap);
                    messageDTO = MessageDTO.builder()
                            .type("다음 플레이어 별 세팅")
                            .data(StarResponseDto.builder()
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
                    historyMap.put(curRound,history);
                    gsb.getPlayers().get(1).setHistories(historyMap);

                    messageDTO = MessageDTO.builder()
                            .type("다음 플레이어 베팅 시작")
                            .data(StarResponseDto.builder()
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
    public History setStarFirst(int round, int idx ,String gameCode, StarDto starDto){
        int weight = getWeight(starDto);
        if(weight<4 && weight >12){
            // TODO: 들어올 수 없는 값 예외 처리
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

    public int getWeight(StarDto starDto){
        return starDto.getGold()*GOLD_WEIGHT
                + starDto.getSilver()*SILVER_WEIGHT
                + starDto.getBronze() *BRONZE_Weight;
    }
     

    public Gsb setGsb(String gameCode) {
        Gsb gsb = gsbRepository.findById(gameCode).orElseThrow();
        gsb.nextRound();
        gsb.setCurrentPlayer(gsb.getPlayers().get(0).getNickname());
        gsbRepository.save(gsb);

        return gsb;

    }

    public Gsb getInfo(String gameCode){
        Gsb gsb = gsbRepository.findById(gameCode).orElseThrow();
        return gsb;

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
