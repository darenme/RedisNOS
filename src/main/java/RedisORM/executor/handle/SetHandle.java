package RedisORM.executor.handle;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.Map;
import java.util.Set;

public interface SetHandle {

    public void zadd(Transaction transaction, String key, Map<String,Double> map);

    public void sadd(Transaction transaction,String key,String... value);

    public Set<String> smember(Jedis jedis, String key);

    public Set<String> zrange(Jedis jedis,String key);

}
