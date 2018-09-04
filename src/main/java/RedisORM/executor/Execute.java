package RedisORM.executor;

import redis.clients.jedis.Jedis;

public interface Execute {

    public void save(Jedis jedis,String id,Object t);

    public Object get(Jedis jedis,String id,Object t);
}
