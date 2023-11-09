package com.maeng.game.domain.awrsp.service;

import com.maeng.game.domain.awrsp.dto.TimerDTO;
import com.maeng.game.domain.awrsp.entity.Game;
import com.maeng.game.domain.awrsp.exception.NotFoundGameException;
import com.maeng.game.domain.awrsp.exception.TimerTypeException;
import com.maeng.game.domain.awrsp.repository.AwrspRepository;
import com.maeng.game.domain.awrsp.repository.TimerRepository;
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
    private final TimerRepository timerRepository;
    private final AwrspRepository awrspRepository;
    private final static String GAME_EXCHANGE = "game";
    private static final int CARD_SUBMIT = 60;
    private static final int DRAW_CARD = 10;
    private static final int PLAYER_WINS = 20;
    private static final int ALL_WINS = 10;

    @Operation(summary = "타이머 시작")
    public void timerStart(String gameCode, String type){
        MessageDTO messageDTO = MessageDTO.builder()
                .type(type)
                .data(this.getTimerSec(type))
                .build();
        template.convertAndSend(GAME_EXCHANGE, "awrsp."+gameCode, messageDTO);
    }

    @Transactional
    @Operation(summary = "타이머 종료")
    public boolean timerEnd(String gameCode, TimerDTO timerDTO){

        // 해당 닉네임 set에 넣기
        Timer timer = timerRepository.findById(gameCode).orElse(null);
        Set<String> set = new HashSet<>();

        if(timer == null){ // 이미 있으면 가져오기
            timer = Timer.builder().gameCode(gameCode).nicknames(set).build();
        }

        set = timer.getNicknames();
        set.add(timerDTO.getNickname());
        timer.setNicknames(set);
        timerRepository.save(timer);

        // 모든 플레이어가 타이머 완료 되었으면 true 아니면 false
        if(set.size() == getCurrentGame(gameCode).getHeadCount()){
            timerRepository.delete(timer); // 타이머 지우고
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
    public int getTimerSec(String type){

        if(type.equals("ENTER_GAME")){ // 해당 타이머가 끝났다는 신호
            return DRAW_CARD;
        }

        if(type.equals("DRAW_CARD")){
            return CARD_SUBMIT;
        }

        if(type.equals("CARD_SUBMIT")){
            return PLAYER_WINS;
        }

        if(type.equals("PLAYER_WINS")){
            return ALL_WINS;
        }

        if(type.equals("ALL_WINS")){
            return CARD_SUBMIT;
        }

        throw new TimerTypeException("잘못된 타이머 타입입니다.");
    }
}
