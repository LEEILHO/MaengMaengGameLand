package com.maeng.game.domain.gsb.service;

import com.maeng.game.domain.gsb.dto.GsbNicknameDto;
import com.maeng.game.domain.jwac.entity.Enter;
import com.maeng.game.domain.jwac.repository.EnterRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class GsbEnterService {
    private final EnterRedisRepository enterRedisRepository;

    @Transactional
    public boolean enter(String gameCode, int headCount, GsbNicknameDto nicknameDto) {
        Enter enter = enterRedisRepository.findById(gameCode)
                .orElse(Enter.builder().gameCode(gameCode).nicknames(new HashSet<>()).build());

        enter.getNicknames().add(nicknameDto.getNickname());

        boolean allEnter = (enter.getNicknames().size() == headCount);

        if(allEnter) {
            enter.getNicknames().clear();
        }

        enterRedisRepository.save(enter);

        return allEnter;
    }


}
