package com.self.redis.springdataredis.listener;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

public class SubscribeMessageListener implements MessageListener{

	public void onMessage(Message message, byte[] pattern) {
		byte[] msg = message.getBody();
		System.out.println("msg--"+new String(msg));
		if(pattern!=null){
			System.out.println("pattern--"+new String(pattern));
		}
	}

}
