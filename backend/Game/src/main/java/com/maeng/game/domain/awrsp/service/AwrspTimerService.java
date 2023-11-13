package com.maeng.game.domain.awrsp.service;

import com.maeng.game.domain.awrsp.dto.TimerDTO;
import com.maeng.game.domain.awrsp.entity.Game;
import com.maeng.game.domain.awrsp.exception.NotFoundGameException;
import com.maeng.game.domain.awrsp.exception.TimerTypeException;
import com.maeng.game.domain.awrsp.repository.AwrspRepository;
import com.maeng.game.domain.awrsp.repository.AwrspTimerRepository;
import com.maeng.game.domain.jwac.entity.Timer;
import com.maeng.game.domain.room.dto.MessageDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class AwrspTimerService {

    private final RabbitTemplate template;
    private final AwrspTimerRepository awrspTimerRepository;
    private final AwrspRepository awrspRepository;
    private final static String GAME_EXCHANGE = "game";
    private static final int CARD_SUBMIT = 60;
    private static final int DRAW_CARD = 10;
    private static final int PLAYER_WINS = 20;
    private static final int ALL_WINS = 5;

    @Operation(summary = "타이머 시작")
    public void timerStart(String gameCode, String type){
        log.info("시작 타이머 전송 : "+type);
        template.convertAndSend(GAME_EXCHANGE, "awrsp."+gameCode, this.getTimerSec(type));
    }

    @Transactional
    @Operation(summary = "타이머 종료")
    public synchronized boolean timerEnd(String gameCode, TimerDTO timerDTO){

        log.info("종료 타이머 : "+timerDTO.toString());
        // 해당 닉네임 set에 넣기
        Timer timer = awrspTimerRepository.findById(gameCode).orElse(null);
        Set<String> set = new HashSet<>();

        if(timer == null){ // 이미 있으면 가져오기
            timer = Timer.builder().gameCode(gameCode).nicknames(set).build();
        }

        set = timer.getNicknames();
        set.add(timerDTO.getNickname());
        timer.setNicknames(set);
        awrspTimerRepository.save(timer);

        Game game = getCurrentGame(gameCode);

        // 모든 플레이어가 타이머 완료 되었으면 true 아니면 false
        if(set.size() >= (game.getHeadCount() - game.getFinishCount())) {
            awrspTimerRepository.delete(timer); // 타이머 지우고
            return true;
        }

        return false;
    }

    public Game getCurrentGame(String gameCode){
        Game game = awrspRepository.findById(gameCode).orElse(null);

        if(game == null){
            throw new NotFoundGameException("게임 정보가 존재하지 않습니다.");
        }

        return game;
    }

    @Operation(summary = "타이머 타입에 따른 타이머 시간 조회")
    public MessageDTO getTimerSec(String type){

        if(type.equals("ENTER_GAME")){ // 해당 타이머가 끝났다는 신호
            return MessageDTO.builder()
                    .type("DRAW_CARD")
                    .data(DRAW_CARD)
                    .build();
        }

        if(type.equals("DRAW_CARD")){
            return MessageDTO.builder()
                    .type("CARD_SUBMIT")
                    .data(CARD_SUBMIT)
                    .build();
        }

        if(type.equals("CARD_SUBMIT")){
            return MessageDTO.builder()
                    .type("PLAYER_WINS")
                    .data(PLAYER_WINS)
                    .build();
        }

        if(type.equals("PLAYER_WINS")){
            return MessageDTO.builder()
                    .type("ALL_WINS")
                    .data(ALL_WINS)
                    .build();
        }

        throw new TimerTypeException("잘못된 타이머 타입입니다.");
    }
}
