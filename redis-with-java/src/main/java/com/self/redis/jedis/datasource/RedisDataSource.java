package com.self.redis.jedis.datasource;

import redis.clients.jedis.ShardedJedis;

public interface RedisDataSource {

	abstract ShardedJedis getRedisClient();
    void returnResource(ShardedJedis shardedJedis);
    void returnResource(ShardedJedis shardedJedis,boolean broken);
	
}
