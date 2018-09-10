package RedisORM.executor.handle;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;

public interface ListHandle {

    public List<String> range(Jedis jedis, String key);

    public void push(Transaction transaction, String key, String... values);

}
