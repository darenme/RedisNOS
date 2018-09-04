package RedisORM.executor.handle;

import redis.clients.jedis.Jedis;

import java.util.Map;

public interface HashHandle {

    public long hset(Jedis jedis, String key, String field, String value);

    public String hget(Jedis jedis,String key,String field);

    public Map<String,String> hgetAll(Jedis jedis,String key);

}
