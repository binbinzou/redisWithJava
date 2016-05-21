package com.self.redis.springdataredis.service;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisZSetCommands.Tuple;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.self.redis.springdataredis.domain.TUser;
import com.self.redis.springdataredis.listener.SubscribeMessageListener;

public class RedisServiceImpl implements IRedisService {

	public RedisTemplate<Serializable, Serializable> redisTemplate;

	public RedisTemplate<Serializable, Serializable> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(
			RedisTemplate<Serializable, Serializable> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public void saveUser(final TUser user) {
		redisTemplate.execute(new RedisCallback<Object>() {

			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.set(
						redisTemplate.getStringSerializer().serialize(
								"user.userid." + user.getId()), redisTemplate
								.getStringSerializer()
								.serialize(user.getName()));
				return null;
			}
		});
	}

	public TUser getUser(final long id) {
		return redisTemplate.execute(new RedisCallback<TUser>() {

			public TUser doInRedis(RedisConnection connection)
					throws DataAccessException {
				byte[] key = redisTemplate.getStringSerializer().serialize(
						"user.userid." + id);
				if (connection.exists(key)) {
					byte[] name = connection.get(key);
					TUser user = new TUser();
					user.setId(id);
					user.setName(redisTemplate.getStringSerializer()
							.deserialize(name));
					return user;
				}
				return null;
			}

		});
	}

	public void delUser(final long id) {
		redisTemplate.execute(new RedisCallback<Object>() {

			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.del(redisTemplate.getStringSerializer().serialize(
						"user.userid." + id));
				return null;
			}

		});
	}

	public void lPushList(final String listName, final String value) {
		redisTemplate.execute(new RedisCallback<Object>() {

			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.lPush(
						redisTemplate.getStringSerializer().serialize(
								listName), redisTemplate
								.getStringSerializer().serialize(value));
				return null;
			}

		});
	}

	public void lPopList(final String listName) {
		redisTemplate.execute(new RedisCallback<Object>() {

			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.lPop(redisTemplate.getStringSerializer().serialize(
						listName));
				return null;
			}

		});

	}

	public void rPushList(final String listName,final String value) {
		redisTemplate.execute(new RedisCallback<Object>() {

			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.rPush(
						redisTemplate.getStringSerializer().serialize(
								listName), redisTemplate
								.getStringSerializer().serialize(value));
				return null;
			}

		});
	}

	public void rPopList(final String listName) {
		redisTemplate.execute(new RedisCallback<Object>() {

			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.rPop(redisTemplate.getStringSerializer().serialize(
						listName));
				return null;
			}

		});
	}

	public Long lenList(final String listName) {
		return redisTemplate.execute(new RedisCallback<Long>() {

			public Long doInRedis(RedisConnection connection)
					throws DataAccessException {
				Long key  = connection.lLen(redisTemplate.getStringSerializer().serialize(listName));
				return key;
			}
		});
	}

	public 	List<String> lRangeList(final String listName,final long start,final long end) {
		return redisTemplate.execute(new RedisCallback<	List<String>>() {

			public 	List<String> doInRedis(RedisConnection connection)
					throws DataAccessException {
				List<byte[]> key  = connection.lRange(redisTemplate.getStringSerializer().serialize(listName), start, end);
				List<String> list = new ArrayList<String>();
				for(byte[] b:key){
					list.add(redisTemplate.getStringSerializer().deserialize(b));
				}
				return list;
			}
		});
	}

	public void sadd(final String listName, final String value) {
		redisTemplate.execute(new RedisCallback<Object>() {

			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.sAdd(redisTemplate.getStringSerializer().serialize(listName), redisTemplate.getStringSerializer().serialize(value));
				return null;
			}
		});
	}

	public void srem(final String listName,final String value) {
		redisTemplate.execute(new RedisCallback<Object>() {

			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.sRem(redisTemplate.getStringSerializer().serialize(listName), redisTemplate.getStringSerializer().serialize(value));
				return null;
			}
		});
	}

	public Set<String> smenbers(final String listName) {
		return redisTemplate.execute(new RedisCallback<Set<String>>() {

			public Set<String> doInRedis(RedisConnection connection)
					throws DataAccessException {
				Set<byte[]> set = connection.sMembers(redisTemplate.getStringSerializer().serialize(listName));
				Set<String> strings = new HashSet<String>();
				for(byte[] b:set){
					strings.add(redisTemplate.getStringSerializer().deserialize(b));
				}
				return strings;
			}
		});
	}

	public Boolean sismembers(final String listName,final String value) {

		return redisTemplate.execute(new RedisCallback<Boolean>() {

			public Boolean doInRedis(RedisConnection connection)
					throws DataAccessException {
				Boolean boolean1 = connection.sIsMember(redisTemplate.getStringSerializer().serialize(listName), redisTemplate.getStringSerializer().serialize(value));
				return boolean1;
			}
		});
		
	}

	public void zadd(final String zsetName, final double score, final String value) {
		redisTemplate.execute(new RedisCallback<Object>() {

			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.zAdd(redisTemplate.getStringSerializer().serialize(zsetName),score, redisTemplate.getStringSerializer().serialize(value));
				return null;
			}
		});
	}

	public Double zscore(final String zsetName, final String value) {
		return redisTemplate.execute(new RedisCallback<Double>() {

			public Double doInRedis(RedisConnection connection)
					throws DataAccessException {
				Double double1 = connection.zScore(redisTemplate.getStringSerializer().serialize(zsetName), redisTemplate.getStringSerializer().serialize(value));
				return double1;
			}
		});
	}

	public Set<String> zrange(final String zsetName,final long start,final long end) {
		return redisTemplate.execute(new RedisCallback<Set<String>>() {

			public Set<String> doInRedis(RedisConnection connection)
					throws DataAccessException {
				Set<byte[]> set = connection.zRange(redisTemplate.getStringSerializer().serialize(zsetName), start, end);
				Set<String> strings = new HashSet<String>();
				for(byte[] b:set){
					strings.add(redisTemplate.getStringSerializer().deserialize(b));
				}
				return strings;
			}
		});
	}

	public void zRem(final String zsetName, final String value) {
		redisTemplate.execute(new RedisCallback<Object>() {

			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.zRem(redisTemplate.getStringSerializer().serialize(zsetName), redisTemplate.getStringSerializer().serialize(value));
				return null;
			}
		});
	}

	public HashMap<String, Double> zrangeWithScore(final String zsetName,final long start,final long end) {
		
		return redisTemplate.execute(new RedisCallback<HashMap<String, Double>>() {

			public HashMap<String, Double> doInRedis(RedisConnection connection)
					throws DataAccessException {
				Set<Tuple> set = connection.zRangeWithScores(redisTemplate.getStringSerializer().serialize(zsetName), start, end);
				HashMap<String, Double> hashMap = new HashMap<String, Double>();
				for(Tuple b:set){
					hashMap.put(redisTemplate.getStringSerializer().deserialize(b.getValue()),b.getScore());
				}
				return hashMap;
			}
		});
	}

	public Long zrank(final String zsetName,final String value) {
		return redisTemplate.execute(new RedisCallback<Long>() {

			public Long doInRedis(RedisConnection connection)
					throws DataAccessException {
				Long set = connection.zRank(redisTemplate.getStringSerializer().serialize(zsetName),redisTemplate.getStringSerializer().serialize(value));
				return set;
			}
		});
	}

	public Long zinsertStore(final String zsetName,final String zsetName1
			) {
		return redisTemplate.execute(new RedisCallback<Long>() {

			public Long doInRedis(RedisConnection connection)
					throws DataAccessException {
				Long set = connection.zInterStore(redisTemplate.getStringSerializer().serialize(zsetName), redisTemplate.getStringSerializer().serialize(zsetName1));
				return set;
			}
		});
	}

	public void hset(final String hName,final String key,final String value) {
		redisTemplate.execute(new RedisCallback<Object>() {

			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.hSet(redisTemplate.getStringSerializer().serialize(hName), redisTemplate.getStringSerializer().serialize(key), redisTemplate.getStringSerializer().serialize(value));
				return null;
			}
		});
	}

	public String hget(final String hName,final String key) {
		return redisTemplate.execute(new RedisCallback<String>() {

			public String doInRedis(RedisConnection connection)
					throws DataAccessException {
				byte[] bs = connection.hGet(redisTemplate.getStringSerializer().serialize(hName), redisTemplate.getStringSerializer().serialize(key));
				String s = redisTemplate.getStringSerializer().deserialize(bs);
				return s;
			}
		});
	}

	public void hmset(final String hName,final Map<String, String> map) {
		redisTemplate.execute(new RedisCallback<Object>() {

			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				Map<byte[], byte[]> mapByte = new HashMap<byte[], byte[]>();
				Set<String> set = map.keySet();
				Iterator<String> iterator = set.iterator();
				while(iterator.hasNext()){
					String key = iterator.next();
					byte[] keyByte = redisTemplate.getStringSerializer().serialize(key);
					byte[] valueByte = redisTemplate.getStringSerializer().serialize(map.get(key));
					mapByte.put(keyByte, valueByte);
				}
				connection.hMSet(redisTemplate.getStringSerializer().serialize(hName), mapByte);
				return null;
			}
		});
	}

	public List<String> hmget(final String hName,final String... value) {
		return redisTemplate.execute(new RedisCallback<List<String>>() {

			public List<String> doInRedis(RedisConnection connection)
					throws DataAccessException {
				byte[][] bs = new byte[value.length][] ;
				int index = 0;
				for(String s : value){
					try {
						bs[index] = s.getBytes("UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					index++;
				}
				List<byte[]> list = connection.hMGet(redisTemplate.getStringSerializer().serialize(hName),bs);
				List<String> strings = new ArrayList<String>();
				for(byte[] b : list){
					strings.add(redisTemplate.getStringSerializer().deserialize(b));
				}
				return strings;
			}
		});
	}

	public Map<String, String> hgetall(final String hName) {
		return redisTemplate.execute(new RedisCallback<Map<String, String>>() {

			public Map<String, String> doInRedis(RedisConnection connection)
					throws DataAccessException {
				Map<byte[],byte[]> mapByte = connection.hGetAll(redisTemplate.getStringSerializer().serialize(hName));
				Map<String, String> map = new HashMap<String, String>();
				Set<byte[]> set = mapByte.keySet();
				Iterator<byte[]> iterator = set.iterator();
				while(iterator.hasNext()){
					byte[] keyByte = iterator.next();
					String key = redisTemplate.getStringSerializer().deserialize(keyByte);
					String value = redisTemplate.getStringSerializer().deserialize(mapByte.get(keyByte));
					map.put(key, value);
				}
				return map;
			}
		});
	}

	public Boolean hexists(final String hName,final String key) {
		return redisTemplate.execute(new RedisCallback<Boolean>() {

			public Boolean doInRedis(RedisConnection connection)
					throws DataAccessException {
				Boolean isExist = connection.hExists(redisTemplate.getStringSerializer().serialize(hName), redisTemplate.getStringSerializer().serialize(key));
				return isExist;
			}
		});
	}

	public Long hdel(final String hName, final String... key) {
		return redisTemplate.execute(new RedisCallback<Long>() {

			public Long doInRedis(RedisConnection connection)
					throws DataAccessException {
				byte[][] bs = new byte[key.length][] ;
				int index = 0;
				for(String s : key){
					try {
						bs[index] = s.getBytes("UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					index++;
				}
				Long list = connection.hDel(redisTemplate.getStringSerializer().serialize(hName),bs);
				return list;
			}
		});
	}

	public Set<String> hkeys(final String hName) {
		return redisTemplate.execute(new RedisCallback<Set<String>>() {

			public Set<String> doInRedis(RedisConnection connection)
					throws DataAccessException {
				Set<byte[]> list = connection.hKeys(redisTemplate.getStringSerializer().serialize(hName));
				Set<String> strings = new HashSet<String>();
				for(byte[] b : list){
					strings.add(redisTemplate.getStringSerializer().deserialize(b));
				}
				return strings;
			}
		});
	}

	public List<String> hvals(final String hName) {
		return redisTemplate.execute(new RedisCallback<List<String>>() {

			public List<String> doInRedis(RedisConnection connection)
					throws DataAccessException {
				List<byte[]> list = connection.hVals(redisTemplate.getStringSerializer().serialize(hName));
				List<String> strings = new ArrayList<String>();
				for(byte[] b : list){
					strings.add(redisTemplate.getStringSerializer().deserialize(b));
				}
				return strings;
			}
		});
	}

	public Long hlen(final String hName) {
		return redisTemplate.execute(new RedisCallback<Long>() {

			public Long doInRedis(RedisConnection connection)
					throws DataAccessException {
				Long l = connection.hLen(redisTemplate.getStringSerializer().serialize(hName));
				return l;
			}
		});
	}

	public Long hincrby(final String hName,final String key,final long count) {
		return redisTemplate.execute(new RedisCallback<Long>() {

			public Long doInRedis(RedisConnection connection)
					throws DataAccessException {
				Long long1 = connection.hIncrBy(redisTemplate.getStringSerializer().serialize(hName),redisTemplate.getStringSerializer().serialize(key),count);
				return long1;
			}
		});
	
	}

	public List<Object> multiOrder() {
		return redisTemplate.execute(new RedisCallback<List<Object>>() {

			public List<Object> doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.multi();
				connection.lPush(redisTemplate.getStringSerializer().serialize("list.multi"), redisTemplate.getStringSerializer().serialize("zbb"));
				connection.lPush(redisTemplate.getStringSerializer().serialize("list.multi"), redisTemplate.getStringSerializer().serialize("zs"));
				List<Object> long1 = connection.exec();
				return long1;
			}
		});
	}

	public String watchOrder() {
		return redisTemplate.execute(new RedisCallback<String>() {

			public String doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.set(redisTemplate.getStringSerializer().serialize("watch.key"), redisTemplate.getStringSerializer().serialize("zb"));
				connection.watch(redisTemplate.getStringSerializer().serialize("watch.key"));
				connection.set(redisTemplate.getStringSerializer().serialize("watch.key"), redisTemplate.getStringSerializer().serialize("zs"));
				connection.multi();
				connection.set(redisTemplate.getStringSerializer().serialize("watch.key"), redisTemplate.getStringSerializer().serialize("ls"));
				connection.exec();
				byte[] bs = connection.get(redisTemplate.getStringSerializer().serialize("watch.key"));
				return redisTemplate.getStringSerializer().deserialize(bs);
			}
		});
	}

	public Boolean expires(final String name,final long timer) {
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection)
					throws DataAccessException {
				Boolean boolean1 = connection.expire(redisTemplate.getStringSerializer().serialize("watch.key"),timer);
				return boolean1;
			}
		});
	}

	public Long ttl(String name) {
		return redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection)
					throws DataAccessException {
				Long timer = connection.ttl(redisTemplate.getStringSerializer().serialize("watch.key"));
				return timer;
			}
		});
	}

	public List<String> bLpop(final String name) {
		return redisTemplate.execute(new RedisCallback<List<String>>() {
			public List<String> doInRedis(RedisConnection connection)
					throws DataAccessException {
				List<byte[]> bs = connection.bLPop(10, redisTemplate.getStringSerializer().serialize(name));
				List<String> strings = new ArrayList<String>();
				for(byte[] bs2 : bs){
					strings.add(redisTemplate.getStringSerializer().deserialize(bs2));
				}
				return strings;
			}
		});
	}

	public Long lPushLists(final String listName,final String... value) {
		return redisTemplate.execute(new RedisCallback<Long>() {

			public Long doInRedis(RedisConnection connection)
					throws DataAccessException {
				byte[][] bs = new byte[value.length][] ;
				int index = 0;
				for(String s : value){
					try {
						bs[index] = s.getBytes("UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					index++;
				}
				Long list = connection.lPush(redisTemplate.getStringSerializer().serialize(listName),bs);
				return list;
			}
		});
	}

	public void subscribe(final String... listName) {
		redisTemplate.execute(new RedisCallback<Object>() {
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				byte[][] bs = new byte[listName.length][] ;
				int index = 0;
				for(String s : listName){
					try {
						bs[index] = s.getBytes("UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					index++;
				}
				SubscribeMessageListener subscribeMessageListener = new SubscribeMessageListener();
				connection.subscribe(subscribeMessageListener, bs);
				return null;
			}
		});
	}

	public Long publist(final String listName,final String value) {
		return redisTemplate.execute(new RedisCallback<Long>() {

			public Long doInRedis(RedisConnection connection)
					throws DataAccessException {
				Long list = connection.publish(redisTemplate.getStringSerializer().serialize(listName),redisTemplate.getStringSerializer().serialize(value));
				return list;
			}
		});
	}

	public void pipelining() {
		redisTemplate.execute(new RedisCallback<Object>() {
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.openPipeline();
				connection.incr(redisTemplate.getStringSerializer().serialize("incr.test"));
				connection.incr(redisTemplate.getStringSerializer().serialize("incr.test"));
				return connection.closePipeline();
			}
		});
	}

	
}
