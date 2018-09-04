package RedisORM.datasource;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class PooledDataSource implements DataSource{

    private String host;

    private int port;

    private JedisPoolConfig config;

    private JedisPool jedispool;


    public PooledDataSource(String host, int port, int total,int idle) {
        this.host = host;
        this.port = port;
        this.config = config;
        this.jedispool = jedispool;

        config = new JedisPoolConfig();
        config.setMaxTotal(total);
        config.setMaxIdle(idle);
        jedispool = new JedisPool(config, host, port);

    }

    @Override
    public Jedis getJedis() {
        return jedispool.getResource();
    }

}
