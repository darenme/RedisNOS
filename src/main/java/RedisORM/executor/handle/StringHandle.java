package RedisORM.executor.handle;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

public interface StringHandle {

    public void set(Transaction transaction, String key, String value);

    public String get(Jedis jedis, String key);

}
