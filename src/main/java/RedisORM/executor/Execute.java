package RedisORM.executor;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.Set;

public interface Execute {

    public void save(Transaction transaction, String id, Object t);

    public Object get(Jedis jedis,String id,Object t);

    public String getProperty();


}
