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
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class AwrspTimerService {

    private final RabbitTemplate template;
    private final TimerRepository timerRepository;
    private final AwrspRepository awrspRepository;
    private final String GAME_EXCHANGE = "game";
    private static final int CARD_OPEN = 15;
    private static final int CARD_SUBMIT = 60;
    private static final int PLAYER_WINS = 20;
    private static final int ALL_WINS = 10;


    @Operation(summary = "타이머 초기화")
    public void timerInit(String gameCode){
        timerRepository.save(Timer.builder()
                .gameCode(gameCode)
                .nicknames(new HashSet<>())
                .build());
    }

    @Operation(summary = "타이머 시작")
    public void timerStart(String gameCode, String type){
        MessageDTO messageDTO = MessageDTO.builder()
                .type(type)
                .data(getTimerSec(type))
                .build();
        template.convertAndSend(GAME_EXCHANGE, "awrsp."+gameCode, messageDTO);
    }

    @Operation(summary = "타이머 종료")
    public boolean timerEnd(String gameCode, TimerDTO timerDTO){

        // 해당 닉네임 set에 넣기
        Timer timer = timerRepository.findByGameCode(gameCode);
        Set<String> set = new HashSet<>();

        if(timer == null){
            timer = Timer.builder().gameCode(gameCode).nicknames(set).build();
        }

        set = timer.getNicknames();
        set.add(timerDTO.getNickname());
        timer.setNicknames(set);
        timerRepository.save(timer);

        // 모든 플레이어가 타이머 완료 되었으면 true 아니면 false
        return set.size() == getCurrentGame(gameCode).getHeadCount();
    }

    public Game getCurrentGame(String gameCode){
        Game game = awrspRepository.findById(gameCode).orElse(null);

        if(game == null){
            throw new NotFoundGameException("게임 정보가 존재하지 않습니다.");
        }

        return game;
    }

    @Operation(summary = "타이머 타입에 따른 다음 타이머 시간 조회")
    public int getTimerSec(String type){

        if(type.equals("ENTER_GAME")){
            return CARD_OPEN;
        }

        if(type.equals("CARD_OPEN")){
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
