package com.maeng.game.global.session.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

@Slf4j
@Configuration
public class StompHandler implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if(StompCommand.CONNECT == accessor.getCommand()){
            log.info(accessor.toString());
            log.info("CONNECT");
        }

        if(StompCommand.SUBSCRIBE == accessor.getCommand()){
            log.info(accessor.toString());
            log.info("SUBSCRIBE");
        }

        if(StompCommand.DISCONNECT == accessor.getCommand()){
            log.info(accessor.toString());
            log.info("DISCONNECT");
        }

        return ChannelInterceptor.super.preSend(message, channel);
    }
}
