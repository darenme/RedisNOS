package RedisORM.executor.handle;

import RedisORM.logging.Log;
import RedisORM.logging.LogFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 这个类实现了所有需要的Jedis操作
 */
public class DefaultHandle implements Handle{

    Log log = LogFactory.getLog(DefaultHandle.class);

    @Override
    public List<String> range(Jedis jedis, String key) {
        if(log.isDebugEnabled()){
            log.debug("$$$$$$$$$ lrang "+ key + " 0  -1");
        }
        return jedis.lrange(key,0,-1);
    }

    @Override
    public void push(Transaction transaction, String key, String... values) {
        if(log.isDebugEnabled()){
            log.debug("$$$$$$$$$ lpush "+ key);
        }
        transaction.del(key);
        transaction.lpush(key,values);
    }

    @Override
    public void zadd(Transaction transaction, String key,Map<String,Double> map) {
        if(log.isDebugEnabled()){
            log.debug("$$$$$$$$$ zadd "+ key);
        }
        transaction.del(key);
        transaction.zadd(key,map);
    }

    @Override
    public void sadd(Transaction transaction, String key, String... value) {
        if(log.isDebugEnabled()){
            log.debug("$$$$$$$$$ sadd "+key);
        }
        transaction.del(key);
        transaction.sadd(key,value);
    }

    @Override
    public Set<String> smember(Jedis jedis, String key) {
        if(log.isDebugEnabled()){
            log.debug("$$$$$$$$$ smembers "+ key);
        }
        return jedis.smembers(key);
    }

    public Set<String> zrange(Jedis jedis,String key){
        if(log.isDebugEnabled()){
            log.debug("$$$$$$$$$ zrange "+ key + " 0 -1");
        }
        return jedis.zrange(key,0,-1);
    }

    @Override
    public void set(Transaction transaction, String key, String value) {
        if(log.isDebugEnabled()){
            log.debug("$$$$$$$$$ set "+key);
        }
        transaction.set(key, value);
    }

    @Override
    public String get(Jedis jedis, String key) {
        if(log.isDebugEnabled()){
            log.debug("$$$$$$$$$ get "+key);
        }
        return jedis.get(key);
    }

    @Override
    public void hset(Transaction transaction, String key, String field, String value) {
        if(log.isDebugEnabled()){
            log.debug("$$$$$$$$$ hset "+ key +" "+field);
        }
        transaction.hset(key,field,value);
    }

    @Override
    public String hget(Jedis jedis, String key, String field) {
        if(log.isDebugEnabled()){
            log.debug("$$$$$$$$$ hget "+ key +" "+field);
        }
        return jedis.hget(key,field);
    }

    @Override
    public Map<String, String> hgetAll(Jedis jedis, String key) {
        if(log.isDebugEnabled()){
            log.debug("$$$$$$$$$ hgetall "+ key );
        }
        return jedis.hgetAll(key);
    }

    @Override
    public byte[] hgetbyte(Jedis jedis, byte[] key, byte[] field) {
        if(log.isDebugEnabled()){
            log.debug("$$$$$$$$$ hget<byte> "+new String(key) +" "+new String(key));
        }
        return jedis.hget(key,field);
    }

    @Override
    public void hsetbyte(Transaction transaction, byte[] key, byte[] field,byte[] value) {
        if(log.isDebugEnabled()){
            log.debug("$$$$$$$$$ hset<byte> "+new String(key) +" "+new String(field));
        }
        transaction.hset(key,field,value);
    }

}