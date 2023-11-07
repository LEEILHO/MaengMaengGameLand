package com.maeng.game.domain.gsb.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.maeng.game.domain.gsb.dto.PlayerSeqDto;
import com.maeng.game.domain.gsb.entity.Gsb;
import com.maeng.game.domain.gsb.entity.Player;
import com.maeng.game.domain.gsb.entity.StartCard;
import com.maeng.game.domain.gsb.repository.GsbRepository;
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
