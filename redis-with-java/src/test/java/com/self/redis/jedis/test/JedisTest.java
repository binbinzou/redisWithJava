package com.self.redis.jedis.test;

import java.util.Date;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.self.redis.jedis.template.RedisClientTemplate;

public class JedisTest {

	private static ApplicationContext ac =  new ClassPathXmlApplicationContext("classpath:spring/applicationContext_jedis.xml");

	public RedisClientTemplate redisClientTemplate;
	
	public JedisTest() {
		super();
		this.redisClientTemplate = (RedisClientTemplate) ac.getBean("redisClientTemplate");
	}

	@Test
	public void set(){
		redisClientTemplate.set("redis-set", "setAAA");
	}
	
	@Test
	public void get(){
		System.out.println(redisClientTemplate.get("redis-set"));
	}
	
	@Test
	public void type(){
		System.out.println(redisClientTemplate.type("redis-set"));
	}
	
	@Test
	public void expire(){
		System.out.println(redisClientTemplate.expire("redis-set", 200));
	}
	
	@Test
	public void expireAt(){
		Date date = new Date();
		Long time = date.getTime();
		System.out.println(redisClientTemplate.expireAt("redis-set", time));
	}
	
	@Test
	public void ttl(){
		System.out.println(redisClientTemplate.ttl("redis-set"));	
	}
	
	@Test
	public void setbit(){
		System.out.println(redisClientTemplate.setbit("redis-set", 1, true));
	}
	
	@Test
	public void getbit(){
		System.out.println(redisClientTemplate.getbit("redis-set", 1));
	}


}
