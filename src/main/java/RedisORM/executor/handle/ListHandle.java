package RedisORM.executor.handle;

import redis.clients.jedis.Jedis;

import java.util.List;

public interface ListHandle {

    public List<String> range(Jedis jedis, String key);

    public long push(Jedis jedis,String key,String... values);

}
