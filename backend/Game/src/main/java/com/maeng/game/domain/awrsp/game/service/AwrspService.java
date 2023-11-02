package com.maeng.game.domain.awrsp.game.service;

import com.maeng.game.domain.room.dto.GameStartDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AwrspService {

    public void gameStart(GameStartDTO gameStartDTO){
        log.info(gameStartDTO.getGameCode());
    }
}
