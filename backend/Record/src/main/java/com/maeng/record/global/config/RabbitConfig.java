package com.maeng.record.global.config;

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

	private static final String RECORD_QUEUE_NAME = "record.queue";
	private static final String JWAC_QUEUE_NAME = "jwac.queue";
	private static final String GSB_QUEUE_NAME = "gsb.queue";
	private static final String AWRSP_QUEUE_NAME = "awrsp.queue";
	private static final String RECORD_EXCHANGE_NAME = "record";
	private static final String RECORD_ROUTING_KEY = "record.*";
	private static final String JWAC_ROUTING_KEY = "record.jwac";
	private static final String GSB_ROUTING_KEY = "record.gsb";
	private static final String AWRSP_ROUTING_KEY = "record.awrsp";

	@Bean
	public Queue record_queue(){
		return new Queue(RECORD_QUEUE_NAME, true);
	}

	@Bean
	public Queue jwac_queue(){
		return new Queue(JWAC_QUEUE_NAME, true);
	}

	@Bean
	public Queue gsb_queue(){
		return new Queue(GSB_QUEUE_NAME, true);
	}

	@Bean
	public Queue awrsp_queue(){
		return new Queue(AWRSP_QUEUE_NAME, true);
	}

	@Bean
	public TopicExchange record_exchange(){
		return new TopicExchange(RECORD_EXCHANGE_NAME);
	}

	@Bean
	public Binding record_binding(){
		return BindingBuilder.bind(record_queue()).to(record_exchange()).with(RECORD_ROUTING_KEY);
	}

	@Bean
	public Binding jwac_binding(){
		return BindingBuilder.bind(jwac_queue()).to(record_exchange()).with(JWAC_ROUTING_KEY);
	}

	@Bean
	public Binding gsb_binding(){
		return BindingBuilder.bind(gsb_queue()).to(record_exchange()).with(GSB_ROUTING_KEY);
	}

	@Bean
	public Binding awrsp_binding(){
		return BindingBuilder.bind(awrsp_queue()).to(record_exchange()).with(AWRSP_ROUTING_KEY);
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
