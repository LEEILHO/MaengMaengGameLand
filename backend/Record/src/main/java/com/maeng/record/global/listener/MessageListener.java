package com.maeng.record.global.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class MessageListener {

	@RabbitListener(queues = "record.queue")
	public void receiveMessage(String message){
		System.out.println("record = " + message);
	}

	@RabbitListener(queues = "jwac.queue")
	public void receiveMessage2(String message){
		System.out.println("jwac = " + message);
	}

	@RabbitListener(queues = "gsb.queue")
	public void receiveMessage3(String message){
		System.out.println("gsb = " + message);
	}

	@RabbitListener(queues = "awrsp.queue")
	public void receiveMessage4(String message){
		System.out.println("awrsp = " + message);
	}
}
