package RedisORM.executor.handle;

import redis.clients.jedis.Jedis;

public interface StringHandle {

    public String set(Jedis jedis, String key, String value);

    public String get(Jedis jedis, String key);

}
