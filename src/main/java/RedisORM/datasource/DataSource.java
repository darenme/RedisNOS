package RedisORM.datasource;

import redis.clients.jedis.Jedis;

public interface DataSource {

    public Jedis getJedis();


}
