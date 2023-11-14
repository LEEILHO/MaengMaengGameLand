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
            log.info("CONNECT");
            log.info(accessor.toString());
            String nickname = accessor.getNativeHeader("nickname").toString().substring(1, accessor.getNativeHeader("nickname").toString().length()-1);
            log.info("[소켓연결] "+nickname);

            sessionRepository.save(Session.builder()
                            .sessionId(accessor.getSessionId())
                            .nickname(nickname)
                    .build());
        }

        if(StompCommand.SUBSCRIBE == accessor.getCommand()){
            String destination = accessor.getNativeHeader("destination").toString().substring(1, accessor.getNativeHeader("destination").toString().length()-1);
            String[] dest = destination.split("/");

            if(dest[2].equals("room")){ // room 구독했을 때
                String[] roomCode = dest[3].split("\\.");
                Session session = sessionRepository.findBySessionId(accessor.getSessionId());
                sessionRepository.save(Session.builder().sessionId(accessor.getSessionId())
                        .nickname(session.getNickname())
                        .roomCode(roomCode[1])
                        .build());
                log.info("[대기방 구독] "+roomCode[1]);
            }

            if(dest[2].equals("game")){ // game을 구독했을 때
                String[] gameCode = dest[3].split("\\.");
                Session session = sessionRepository.findBySessionId(accessor.getSessionId());
                sessionRepository.save(Session.builder().sessionId(accessor.getSessionId())
                        .nickname(session.getNickname())
                        .gameCode(gameCode[1])
                        .build());
                log.info("[게임 구독] : "+gameCode[1]);
            }
        }

        if(StompCommand.DISCONNECT == accessor.getCommand()){
            log.info("DISCONNECT");
            Session session = sessionRepository.findBySessionId(accessor.getSessionId());
            log.info("[연결끊김] "+session.getNickname());
//            if(session.getRoomCode() != null){ // 대기방에 참여중이라면
//                log.info("[대기방 퇴장] "+session.getNickname());
//                roomService.exitRoom(session.getRoomCode(), PlayerDTO.builder().nickname(session.getNickname()).build());
//            }
//
//            if(session.getGameCode() != null){ // 게임에 참여중이라면
//                log.info("[연결끊김] "+session.getNickname()+" 게임 중 퇴장");
//                roomService.disconnectPlayer(session.getGameCode(), session.getNickname());
//            }

            sessionRepository.delete(session);
        }

        return ChannelInterceptor.super.preSend(message, channel);
    }
}
