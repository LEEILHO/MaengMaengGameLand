package com.maeng.user.global.config;

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

@Configuration
@EnableRabbit
public class RabbitConfig {

	@Value("${spring.rabbitmq.host}")
	private String HOST;
	@Value("${spring.rabbitmq.username}")
	private String USERNAME;
	@Value("${spring.rabbitmq.password}")
	private String PASSWORD;
	@Value("${spring.rabbitmq.port}")
	private int PORT;

	private static final String REGISTER_QUEUE_NAME = "edit.queue";
	private static final String JWAC_QUEUE_NAME = "score.jwac";
	private static final String AWRSP_QUEUE_NAME = "score.awrsp";
	private static final String GSB_QUEUE_NAME = "score.gsb";
	private static final String REGISTER_EXCHANGE_NAME = "user";
	private static final String SCORE_EXCHANGE_NAME = "score";
	private static final String REGISTER_ROUTING_KEY = "edit.#";
	private static final String JWAC_ROUTING_KEY = "score.jwac";
	private static final String AWRSP_ROUTING_KEY = "score.awrsp";
	private static final String GSB_ROUTING_KEY = "score.gsb";

	@Bean
	public Queue edit_queue(){
		return new Queue(REGISTER_QUEUE_NAME, true);
	}

	@Bean
	public Queue jwac_queue(){
		return new Queue(JWAC_QUEUE_NAME, true);
	}

	@Bean
	public Queue awrsp_queue(){
		return new Queue(AWRSP_QUEUE_NAME, true);
	}

	@Bean
	public Queue gsb_queue(){
		return new Queue(GSB_QUEUE_NAME, true);
	}

	@Bean
	public TopicExchange user_exchange(){
		return new TopicExchange(REGISTER_EXCHANGE_NAME);
	}

	@Bean
	public TopicExchange score_exchange(){
		return new TopicExchange(SCORE_EXCHANGE_NAME);
	}

	@Bean
	public Binding edit_binding(){
		return BindingBuilder.bind(edit_queue()).to(user_exchange()).with(REGISTER_ROUTING_KEY);
	}

	@Bean
	public Binding jwac_binding(){
		return BindingBuilder.bind(jwac_queue()).to(score_exchange()).with(JWAC_ROUTING_KEY);
	}

	@Bean
	public Binding awrsp_binding(){
		return BindingBuilder.bind(awrsp_queue()).to(score_exchange()).with(AWRSP_ROUTING_KEY);
	}

	@Bean
	public Binding gsb_binding(){
		return BindingBuilder.bind(gsb_queue()).to(score_exchange()).with(GSB_ROUTING_KEY);
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
