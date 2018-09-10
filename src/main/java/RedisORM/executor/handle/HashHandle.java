package RedisORM.executor.handle;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.Map;

public interface HashHandle {

    public void hset(Transaction transaction, String key, String field, String value);

    public String hget(Jedis jedis,String key,String field);

    public Map<String,String> hgetAll(Jedis jedis,String key);

    public byte[] hgetbyte(Jedis jedis,byte[] key,byte[] field);

    public void hsetbyte(Transaction transaction,byte[] key,byte[] field,byte[] value);

}
