package RedisORM.executor.handle;

import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class DefaultHandle implements Handle{

    @Override
    public List<String> range(Jedis jedis, String key) {

        return jedis.lrange(key,0,-1);
    }

    @Override
    public long push(Jedis jedis, String key, String... values) {

        return jedis.lpush(key,values);

    }

    @Override
    public long zadd(Jedis jedis, String key,Map<String,Double> map) {
        return jedis.zadd(key,map);
    }

    @Override
    public long sadd(Jedis jedis, String key, String... value) {
        return jedis.sadd(key,value);
    }


    @Override
    public Set<String> smember(Jedis jedis, String key) {
        return jedis.smembers(key);
    }

    public Set<String> zrange(Jedis jedis,String key){
        return jedis.zrange(key,0,-1);
    }

    @Override
    public String set(Jedis jedis, String key, String value) {
        return jedis.set(key, value);
    }

    @Override
    public String get(Jedis jedis, String key) {
        return jedis.get(key);
    }

    @Override
    public long hset(Jedis jedis, String key, String field, String value) {
        return jedis.hset(key,field,value);
    }

    @Override
    public String hget(Jedis jedis, String key, String field) {
        return jedis.hget(key,field);
    }

    @Override
    public Map<String, String> hgetAll(Jedis jedis, String key) {
        return jedis.hgetAll(key);
    }

}
