package RedisORM.executor.handle;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class DefaultHandle implements Handle{

    @Override
    public List<String> range(Jedis jedis, String key) {

        return jedis.lrange(key,0,-1);
    }

    @Override
    public void push(Transaction transaction, String key, String... values) {
        transaction.lpush(key,values);
    }

    @Override
    public void zadd(Transaction transaction, String key,Map<String,Double> map) {
        transaction.zadd(key,map);
    }

    @Override
    public void sadd(Transaction transaction, String key, String... value) {
        transaction.sadd(key,value);
    }


    @Override
    public Set<String> smember(Jedis jedis, String key) {
        return jedis.smembers(key);
    }

    public Set<String> zrange(Jedis jedis,String key){
        return jedis.zrange(key,0,-1);
    }

    @Override
    public void set(Transaction transaction, String key, String value) {
        transaction.set(key, value);
    }

    @Override
    public String get(Jedis jedis, String key) {
        return jedis.get(key);
    }

    @Override
    public void hset(Transaction transaction, String key, String field, String value) {
        transaction.hset(key,field,value);
    }

    @Override
    public String hget(Jedis jedis, String key, String field) {
        return jedis.hget(key,field);
    }

    @Override
    public Map<String, String> hgetAll(Jedis jedis, String key) {
        return jedis.hgetAll(key);
    }

    @Override
    public byte[] hgetbyte(Jedis jedis, byte[] key, byte[] field) {
        return jedis.hget(key,field);
    }

    @Override
    public void hsetbyte(Transaction transaction, byte[] key, byte[] field,byte[] value) {
        transaction.hset(key,field,value);
    }

}