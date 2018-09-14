package RedisORM.datasource;

import redis.clients.jedis.Jedis;

/**
 * 用于获取数据源
 */
public interface DataSource {

    public Jedis getJedis();


}
