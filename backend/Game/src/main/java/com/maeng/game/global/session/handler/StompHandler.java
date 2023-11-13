package com.maeng.game.global.session.handler;

import com.maeng.game.domain.room.dto.PlayerDTO;
import com.maeng.game.domain.room.service.RoomService;
import com.maeng.game.global.session.entity.Session;
import com.maeng.game.global.session.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class StompHandler implements ChannelInterceptor {
    private final SessionRepository sessionRepository;
    private final RoomService roomService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if(StompCommand.CONNECT == accessor.getCommand()){
            log.info(accessor.toString());
            String nickname = accessor.getNativeHeader("nickname").toString();
            nickname = nickname.substring(1, nickname.length()-1);

            log.info("CONNECT");
            sessionRepository.save(Session.builder()
                            .sessionId(accessor.getSessionId())
                            .nickname(nickname)
                    .build());
        }

        if(StompCommand.DISCONNECT == accessor.getCommand()){
            log.info(accessor.toString());
           // log.info(accessor.getNativeHeader("nickname").toString());
            log.info("DISCONNECT");
            Session session = sessionRepository.findBySessionId(accessor.getSessionId());

            if(session.getRoomCode() != null){ // 대기방에 참여중이라면
                // TODO : 대기방 퇴장 처리
                log.info("[연결끊김] "+session.getNickname()+" 대기방 퇴장");
                roomService.exitRoom(session.getRoomCode(), PlayerDTO.builder().nickname(session.getNickname()).build());
            }

            if(session.getGameCode() != null){ // 게임 코드만 있을 것
                log.info("[연결끊김] "+session.getNickname()+" 게임 중 퇴장");
                roomService.disconnectPlayer(session.getRoomCode(), session.getGameCode(), session.getNickname());
            }

            sessionRepository.delete(session);
        }

        return ChannelInterceptor.super.preSend(message, channel);
    }
}
