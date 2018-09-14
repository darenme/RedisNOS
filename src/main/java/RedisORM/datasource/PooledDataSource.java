package RedisORM.datasource;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 池化的数据源
 */
public class PooledDataSource implements DataSource{

    // 主机名
    private String host;

    // 端口
    private int port;

    // Jedis池的配置
    private JedisPoolConfig config;

    // Jedis池
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
