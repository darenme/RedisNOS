package RedisORM.executor.handle;

import redis.clients.jedis.Jedis;

import java.util.Map;
import java.util.Set;

public interface SetHandle {

    public long zadd(Jedis jedis, String key,Map<String,Double> map);

    public long sadd(Jedis jedis,String key,String... value);

    public Set<String> smember(Jedis jedis, String key);

    public Set<String> zrange(Jedis jedis,String key);

}
