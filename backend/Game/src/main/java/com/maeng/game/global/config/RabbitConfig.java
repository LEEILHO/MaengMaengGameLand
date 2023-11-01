package com.maeng.game.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
@EnableRabbit
public class RabbitConfig {

    private static final String ROOM_QUEUE_NAME = "room.queue";
    private static final String GAME_QUEUE_NAME = "game.queue";
    private static final String ROOM_EXCHANGE_NAME = "room";
    private static final String GAME_EXCHANGE_NAME = "game";
    private static final String ROOM_ROUTING_KEY = "room.*";
    private static final String GAME_ROUTING_KEY = "*.game.*"; // *.game.게임종류.방코드

    @Value("${spring.rabbitmq.host}")
    private String HOST;
    @Value("${spring.rabbitmq.username}")
    private String USERNAME;
    @Value("${spring.rabbitmq.password}")
    private String PASSWORD;
    @Value("${spring.rabbitmq.port}")
    private int PORT;

    // Queue 등록
    @Bean
    public Queue room_queue(){
        return new Queue(ROOM_QUEUE_NAME, true);
    }

    @Bean
    public Queue game_queue(){
        return new Queue(GAME_QUEUE_NAME, true);
    }

    // Exchange 등록
    @Bean
    public TopicExchange room_exchange(){
        return new TopicExchange(ROOM_EXCHANGE_NAME);
    }

    @Bean
    public TopicExchange game_exchange(){
        return new TopicExchange(GAME_EXCHANGE_NAME);
    }

    // Exchange와 Queue 바인딩
    @Bean
    public Binding room_binding(){
        return BindingBuilder.bind(room_queue()).to(room_exchange()).with(ROOM_ROUTING_KEY);
    }

    @Bean
    public Binding game_binding(){
        return BindingBuilder.bind(game_queue()).to(game_exchange()).with(GAME_ROUTING_KEY);
    }

    @Bean
    public ConnectionFactory connectionFactory(){
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setHost(HOST);
        factory.setPort(PORT);
        factory.setUsername(USERNAME);
        factory.setPassword(PASSWORD);

        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }
}
