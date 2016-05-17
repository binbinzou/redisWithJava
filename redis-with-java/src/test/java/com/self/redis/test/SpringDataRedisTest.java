package com.self.redis.test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.self.redis.domain.TUser;
import com.self.redis.service.IRedisService;
import com.self.redis.service.RedisServiceImpl;

public class SpringDataRedisTest {

	private static ApplicationContext ac =  new ClassPathXmlApplicationContext("classpath:spring/applicationContext.xml");

	public IRedisService redisService ; 

	
	public SpringDataRedisTest() {
		super();
		this.redisService = (IRedisService) ac.getBean("redisService");
	}
	
	@Test
	public void saveUser(){
		IRedisService redisService = (IRedisService) ac.getBean("redisService");
		TUser user = new TUser();
        user.setId(1);
        user.setName("obama");
        redisService.saveUser(user);
	}
	@Test
	public void getUser(){
		IRedisService redisService = (IRedisService) ac.getBean("redisService");
		TUser tUser = redisService.getUser(1);
        System.out.println(tUser.getName());
        
	}
	@Test
	public void delUser(){
		IRedisService redisService = (IRedisService) ac.getBean("redisService");
		redisService.delUser(1);
	}
	@Test
	public void lpushList(){
		IRedisService redisService = (IRedisService) ac.getBean("redisService");
		redisService.lPushList("user.list", "zbb1");
	}
	@Test
	public void rpushList(){
		IRedisService redisService = (IRedisService) ac.getBean("redisService");
		redisService.rPushList("user.list", "zbb3");
	}
	@Test
	public void lpopList(){
		IRedisService redisService = (IRedisService) ac.getBean("redisService");
		redisService.lPopList("user.list");
	}
	@Test
	public void rpopList(){
		IRedisService redisService = (IRedisService) ac.getBean("redisService");
		redisService.rPopList("user.list");
	}
	@Test
	public void lenList(){
		IRedisService redisService = (IRedisService) ac.getBean("redisService");
		Long long1 = redisService.lenList("user.list");
		System.out.println(long1);
	}
	@Test
	public void rangeList(){
		IRedisService redisService = (IRedisService) ac.getBean("redisService");
		List<String> list = redisService.lRangeList("user.list", 0, 10);
		for(String s : list){
			System.out.println(s);
		}
	}
	
	@Test
	public void zadd(){
		IRedisService redisService = (IRedisService) ac.getBean("redisService");
		redisService.zadd("user.zset", 80, "zbb2");
	}
	@Test
	public void zrem(){
		IRedisService redisService = (IRedisService) ac.getBean("redisService");
		redisService.zRem("user.zset", "zbb2");
	}
	@Test
	public void zscore(){
		IRedisService redisService = (IRedisService) ac.getBean("redisService");
		System.out.println(redisService.zscore("user.zset", "zbb2"));
	}
	@Test
	public void zrange(){
		IRedisService redisService = (IRedisService) ac.getBean("redisService");
		Set<String> list = redisService.zrange("user.zset",0,10);
		for(String s : list){
			System.out.println(s);
		}
	}
	@Test
	public void sismembers(){
		IRedisService redisService = (IRedisService) ac.getBean("redisService");
		System.out.println(redisService.sismembers("user.set", "zbb12"));
	}
	@Test
	public void zrangeWithScore(){
		IRedisService redisService = (IRedisService) ac.getBean("redisService");
		HashMap<String, Double> hashMap = redisService.zrangeWithScore("user.zset",0,10);
		Set<String> s = hashMap.keySet();
		Iterator<String> iterator = s.iterator();
		while(iterator.hasNext()){
			String str = iterator.next();
			System.out.println(str);
			System.out.println(hashMap.get(str));
		}
	}
	@Test
	public void zrank(){
		IRedisService redisService = (IRedisService) ac.getBean("redisService");
		System.out.println(redisService.zrank("user.zset", "zbb3"));
	}
	@Test
	public void zinsertStore(){
		IRedisService redisService = (IRedisService) ac.getBean("redisService");
		System.out.println(redisService.zinsertStore("user.zset", "user.zset1"));
	}
	
	@Test
	public void hset(){
		IRedisService redisService = (IRedisService) ac.getBean("redisService");
		redisService.hset("user.hset", "name","邹斌斌");
		redisService.hset("user.hset", "age","24");
		redisService.hset("user.hset", "sex","boy");
	}
	@Test
	public void hget(){
		IRedisService redisService = (IRedisService) ac.getBean("redisService");
		System.out.println(redisService.hget("user.hset", "name"));
		System.out.println(redisService.hget("user.hset", "age"));
		System.out.println(redisService.hget("user.hset", "sex"));
	}
	
	@Test
	public void hMSet(){
		IRedisService redisService = (IRedisService) ac.getBean("redisService");
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", "张三");
		map.put("age", "30");
		map.put("sex", "男");
		redisService.hmset("user.hmset",map);
	}
	@Test
	public void hMGet(){
		IRedisService redisService = (IRedisService) ac.getBean("redisService");
		String s[]  = {"name","age","sex"};
		List<String> list = redisService.hmget("user.hmset", s);
		for(String ss: list){
			System.out.println(ss);
		}
	}

	@Test
	public void hGetAll(){
		IRedisService redisService = (IRedisService) ac.getBean("redisService");
		Map<String,String> map = redisService.hgetall("user.hmset");
		Set<String> s = map.keySet();
		Iterator<String> iterator = s.iterator();
		while(iterator.hasNext()){
			String str = iterator.next();
			System.out.println(str);
			System.out.println(map.get(str));
		}
	}
	
	@Test
	public void hexists(){
		IRedisService redisService = (IRedisService) ac.getBean("redisService");
		System.out.println(redisService.hexists("user.hmset", "name"));
	}
	
	@Test
	public void hdel(){
		IRedisService redisService = (IRedisService) ac.getBean("redisService");
		String s[]  = {"name"};
		System.out.println(redisService.hdel("user.hmset", s));
	}
	
	@Test
	public void hkeys(){
		IRedisService redisService = (IRedisService) ac.getBean("redisService");
		Set<String> list = redisService.hkeys("user.hmset");
		for(String ss: list){
			System.out.println(ss);
		}
	}
	
	@Test
	public void hvals(){
		IRedisService redisService = (IRedisService) ac.getBean("redisService");
		List<String> list = redisService.hvals("user.hmset");
		for(String ss: list){
			System.out.println(ss);
		}
	}
	
	@Test
	public void hlen(){
		IRedisService redisService = (IRedisService) ac.getBean("redisService");
		System.out.println(redisService.hlen("user.hmset"));
	}
	
	@Test
	public void hincrby(){
		IRedisService redisService = (IRedisService) ac.getBean("redisService");
		System.out.println(redisService.hincrby("user.hmset","age",2));
	}
	@Test
	public void multiOrder(){
		List<Object> list = redisService.multiOrder();
		for(Object o : list){
			System.out.println(o.toString());
		}
	}
	@Test
	public void watchOrder(){
		System.out.println(redisService.watchOrder());
	}
	@Test
	public void expires(){
		System.out.println(redisService.expires("user.zset1",10));
	}
	@Test
	public void ttl(){
		System.out.println(redisService.ttl("user.zset1"));
	}
}
