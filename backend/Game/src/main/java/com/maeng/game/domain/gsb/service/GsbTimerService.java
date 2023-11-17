package com.maeng.game.domain.gsb.service;

import com.maeng.game.domain.gsb.repository.GsbTimerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GsbTimerService {
    private final GsbTimerRepository gsbTimerRepository;

    private final static int TURN_TIME = 30;

//    @Transactional
//    public boolean timerStart(String gameCode, )
}
