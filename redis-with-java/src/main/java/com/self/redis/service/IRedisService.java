package com.self.redis.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.self.redis.domain.TUser;

public interface IRedisService {

	void saveUser(final TUser user);
	
	TUser getUser(final long id);

	void delUser(final long  i);
	
	void lPushList(final String listName,final String value);
	
	void lPopList(final String listName);
	
	void rPushList(final String listName,final String value);
	
	void rPopList(final String listName);
	
	Long lenList(final String listName);
	
	List<String> lRangeList(final String listName,final long start,final long end);
	
	void sadd(final String setName,final String value);
	
	void srem(final String setName,final String value);
	
	Set<String> smenbers(final String listName);
	
	Boolean sismembers(final String listName,final String value);
	
	void zadd(final String zsetName,final double score,final String value);
	
	Double zscore(final String zsetName,final String value);
	
	Set<String> zrange(final String zsetName,final long start,final long end);
	
	void zRem(final String zsetName,final String value);
	
	HashMap<String, Double> zrangeWithScore(final String zsetName,final long start,final long end);
	
	Long zrank(final String zsetName,final String value);
	
	Long zinsertStore(final String zsetName,final String zsetName1);
	
	void hset(final String hName,final String key,final String value);
	
	String hget(final String hName,final String key);
	
	void hmset(final String hName,final Map<String, String> map);
	
	List<String> hmget(final String hName,final String... value);
	
	Map<String, String> hgetall(final String hName);
	
	Boolean hexists(final String hName,final String key);
	
	Long hdel(final String hName,final String... key);
	
	Set<String> hkeys(final String hName);
	
	List<String> hvals(final String hName);
	
	Long hlen(final String hName);
	
	
	
}
